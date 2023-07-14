package com.fias.ddrhighspeed.search.songsearch

import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.spreadsheet.TestSpreadSheetService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class EstimateByNameViewModelTest {
    private lateinit var viewModel: EstimateByNameViewModel
    private val correctUrlBase =
        "https://docs.google.com/spreadsheets/d/1BPqdsURvQYveHLNhREJIUC440TFPj5JzwizKkhdIKQY/export?format=tsv&gid="
    private val service = TestSpreadSheetService(correctUrlBase)

    @Before
    fun setUp() {
        val mockDatabase = mock<IDatabase>()

        val mockSongs = listOf(
            Song(
                1, "Test Song 1", "composer1", "A3", "123",
                null, null, 123.0, null,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "", 0
            ),
            Song(
                2, "Test Song 2", "composer2", "DDR", "234-567",
                123.0, 234.0, 345.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1
            ),
        )
        val mockSongs2 = listOf(
            Song(
                2, "Test Song 2", "composer2", "DDR", "234-567",
                123.0, 234.0, 345.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1
            ),
        )
        whenever(mockDatabase.searchSongsByName("Test")).thenReturn(mockSongs)
        whenever(mockDatabase.searchSongsByName("")).thenReturn(null)
        whenever(mockDatabase.getNewSongs()).thenReturn(mockSongs2)

        viewModel = EstimateByNameViewModel(mockDatabase, service)
    }

    @Test
    fun searchSongsByName_returns_correct_list_when_searchWord_is_not_empty() {
        viewModel.searchWord.value = "Test"

        // When
        val result = viewModel.searchSongsByName()

        val expected = listOf(
            SongData(
                1, "Test Song 1", "composer1", "A3", "123",
                null, null, 123.0, null,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "", 0
            ),
            SongData(
                2, "Test Song 2", "composer2", "DDR", "234-567",
                123.0, 234.0, 345.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1
            ),
        )
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun searchSongsByName_returns_correct_list_when_searchWord_is_empty() {
        viewModel.searchWord.value = ""

        // When
        val result = viewModel.searchSongsByName()

        val expected = listOf(
            SongData(
                2, "Test Song 2", "composer2", "DDR", "234-567",
                123.0, 234.0, 345.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1
            ),
        )
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun resetSearchWord() {
        viewModel.searchWord.value = "test"

        // When
        viewModel.resetSearchWord()

        // Then
        assertEquals("", viewModel.searchWord.value)
    }

    @Test
    fun checkNewVersionAvailable_available() = runTest {
        assertFalse(viewModel.updateAvailable.value!!)
        assertNull(viewModel.localDataVersion.value)

        val intLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val intExpectedValues = listOf(10) // 期待する値のリスト
        val intActualValues = mutableListOf<Int>()

        val intObserver = Observer<Int> {
            intActualValues.add(it)
            intLatch.countDown()
        }
        viewModel.localDataVersion.observeForever(intObserver)

        viewModel.checkNewDataVersionAvailable(10)

        assertTrue(viewModel.updateAvailable.value!!)
        assertEquals(intExpectedValues, intActualValues)
    }

    @Test
    fun checkNewVersionAvailable_unavailable() = runTest {
        assertFalse(viewModel.updateAvailable.value!!)
        assertNull(viewModel.localDataVersion.value)

        val localVersionLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedLocalVersions = listOf(Int.MAX_VALUE) // 期待する値のリスト
        val actualLocalVersions = mutableListOf<Int>()

        viewModel.localDataVersion.observeForever {
            actualLocalVersions.add(it)
            localVersionLatch.countDown()
        }

        viewModel.checkNewDataVersionAvailable(Int.MAX_VALUE)

        assertFalse(viewModel.updateAvailable.value!!)
        assertEquals(expectedLocalVersions, actualLocalVersions)
    }

    @Test
    fun downloadSongData() = runTest {
        val expectedSourceVersion = 2023061121
        viewModel.errorMessage.observeForever {
            fail()
        }

        val localVersionLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedLocalVersions = listOf(expectedSourceVersion) // 期待する値のリスト
        val actualLocalVersions = mutableListOf<Int>()

        viewModel.localDataVersion.observeForever {
            actualLocalVersions.add(it)
            localVersionLatch.countDown()
        }

        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }

        viewModel.downloadSongData()
        withContext(Dispatchers.IO) {
            assertTrue(localVersionLatch.await(3, TimeUnit.SECONDS))
            assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
        }
        assertFalse(viewModel.updateAvailable.value!!)
        assertEquals(expectedLocalVersions, actualLocalVersions)
        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(expectedSourceVersion, viewModel.sourceDataVersion)
    }

    @Test
    fun downloadSongData_failToAccess() = runTest {
        val viewModel =
            EstimateByNameViewModel(mock(), TestSpreadSheetService("http://google.com"))

        val errorMessageLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedErrorMessages = listOf(
            "データの取得に失敗しました。\n" +
                    "しばらく後に再実施していただくか、左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
        ) // 期待する値のリスト
        val actualErrorMessage = mutableListOf<String>()

        viewModel.errorMessage.observeForever {
            actualErrorMessage.add(it)
            errorMessageLatch.countDown()
        }

        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }

        viewModel.downloadSongData()
        withContext(Dispatchers.IO) {
            assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
        }
        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(expectedErrorMessages, actualErrorMessage)
        // REVISIT: 本来はinitで-1に設定されるべきだが非同期処理が間に合わないらしいので初期値で判定
        assertEquals(0, viewModel.sourceDataVersion)
        // init含め、一度もversionの値が書き換わらないのでデフォルトのfalse
        assertFalse(viewModel.updateAvailable.value!!)
    }

    @Test
    fun downloadSongData_InconsistentData() = runTest {
        val viewModel =
            EstimateByNameViewModel(
                mock(), TestSpreadSheetService(
                    "https://docs.google.com/spreadsheets/d/10BCNI4sarU2G32P7FLMchKa3IgQThNxFfr3E90L11cw/export?format=tsv&gid="
                )
            )

        val errorMessageLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedErrorMessages = listOf(
            "データに不整合があります。\n" +
                    "左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
        ) // 期待する値のリスト
        val actualErrorMessage = mutableListOf<String>()

        viewModel.errorMessage.observeForever {
            actualErrorMessage.add(it)
            errorMessageLatch.countDown()
        }

        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }

        viewModel.downloadSongData()
        withContext(Dispatchers.IO) {
            assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
        }
        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(expectedErrorMessages, actualErrorMessage)
        // REVISIT: 本来はinitで-1に設定されるべきだが非同期処理が間に合わないらしいので初期値で判定
        assertEquals(0, viewModel.sourceDataVersion)
        // init含め、一度もversionの値が書き換わらないのでデフォルトのfalse
        assertFalse(viewModel.updateAvailable.value!!)
    }
}