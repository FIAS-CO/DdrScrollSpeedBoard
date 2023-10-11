package com.fias.ddrhighspeed.search

import com.fias.ddrhighspeed.data.TestDataVersionDataStore
import com.fias.ddrhighspeed.shared.cache.Course
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Movie
import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId
import com.fias.ddrhighspeed.shared.spreadsheet.FailureResult
import com.fias.ddrhighspeed.shared.spreadsheet.ISpreadSheetService
import com.fias.ddrhighspeed.shared.spreadsheet.SSDataResult
import com.fias.ddrhighspeed.shared.spreadsheet.SuccessResult
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(RobolectricTestRunner::class)
class DataUpdateViewModelTest {

    private lateinit var mockDatabase: IDatabase
    private val songName = SongName(
        1, "Test Song 1", "Test Song 1 ruby", "A3", 123,
        12, 1, 2, 3,
        1, 2, 3, 4, 5, 6,
    )
    private val songNames = listOf(songName, songName)

    private val songProp = SongProperty(
        1, 1, "Test Song 1", 131.0, 200.0,
        12.0, 1000.0, ""
    )
    private val musicProperties = listOf(songProp, songProp)

    private val shockArrow = ShockArrowExists(1, "xx")
    private val shockArrowExists = listOf(shockArrow, shockArrow)

    private val webMusicId = WebMusicId(1, "webId", "name", 0)
    private val webMusicIds = listOf(webMusicId, webMusicId)

    private val movie = Movie(1, 2, "Difficult", "site", "movieId", "songName")
    private val movies = listOf(movie, movie)

    private val course = Course(1, "name", 0L, 1, 2, 3, 4, 5, 6, 1, 2, 0)
    private val courses = listOf(course, course)

    private var dataStore = TestDataVersionDataStore()

    @Before
    fun setUp() {
        mockDatabase = mock()

        val mockSongs = listOf(
            Song(
                1, "Test Song 1", "composer1", "A3", "123",
                null, null, 123.0, null,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "", 0, null
            ),
            Song(
                2, "Test Song 2", "composer2", "DDR", "234-567",
                123.0, 234.0, 345.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
            )
        )
        whenever(mockDatabase.getNewSongs()).thenReturn(mockSongs)
        // TODO reInitializeメソッドの引数をテストする
//        doAnswer { invocation: InvocationOnMock ->
//            val arg = invocation.getArgument<List<SongName>>(0) // 0は最初の引数を示す
//            assertEquals(songNames, arg)
//            null // doAnswerは最後に値を返さなければならないので、Voidの場合はnullを返す
//        }.whenever(mockDatabase).reinitializeSongNames(any())

        dataStore = TestDataVersionDataStore()
    }

    @Test
    fun セットアップ_初回() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 100
            ssDataResult = SuccessResult(
                100,
                songNames,
                musicProperties,
                shockArrowExists,
                webMusicIds,
                movies,
                courses
            )
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)

        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val isLoadingLatch = CountDownLatch(expectedIsLoadings.size) // 引数は期待する値の変更の回数
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.errorMessage.observeForever {
            Assert.fail(it)
        }
        viewModel.initialize()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(100, viewModel.localDataVersion.value)
        assertEquals(100, dataStore.actualInputVersion)
    }

    @Test
    fun セットアップ_2回め以降_ローカルが最新状態() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 100
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)
        dataStore.expectedOutputVersion = 100

        val isLoadingLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedIsLoadings = listOf(false) // 期待する値のリスト
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.errorMessage.observeForever {
            Assert.fail(it)
        }
        viewModel.initialize()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(100, viewModel.localDataVersion.value)
        assertEquals(false, viewModel.updateAvailable.value)
        assertEquals(-1, dataStore.actualInputVersion) // 変化なし
    }

    @Test
    fun セットアップ_2回め以降_外部ソースが最新状態() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 1000
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)
        dataStore.expectedOutputVersion = 100

        val isLoadingLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
        val expectedIsLoadings = listOf(false) // 期待する値のリスト
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.errorMessage.observeForever {
            Assert.fail(it)
        }
        viewModel.initialize()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(100, viewModel.localDataVersion.value)
        assertEquals(true, viewModel.updateAvailable.value)
        assertEquals(-1, dataStore.actualInputVersion) // 変化なし
    }

    @Test
    fun ダウンロード実行() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 999
            ssDataResult = SuccessResult(
                newDataVersion,
                songNames,
                musicProperties,
                shockArrowExists,
                webMusicIds,
                movies,
                courses
            )
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)
        dataStore.expectedOutputVersion = 100

        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val isLoadingLatch = CountDownLatch(expectedIsLoadings.size) // 引数は期待する値の変更の回数
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.errorMessage.observeForever {
            Assert.fail(it)
        }
        viewModel.initialize()

        viewModel.downloadSongData()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(999, viewModel.localDataVersion.value)
        assertEquals(false, viewModel.updateAvailable.value)
        assertEquals(999, dataStore.actualInputVersion) // 変化なし
    }

    @Test
    fun ダウンロード実行_通信等の原因でダウンロード失敗() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 999
            ssDataResult = SuccessResult(
                newDataVersion,
                listOf(), // 不整合
                musicProperties,
                shockArrowExists,
                webMusicIds,
                movies,
                courses
            )
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)
        dataStore.expectedOutputVersion = 100

        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val isLoadingLatch = CountDownLatch(expectedIsLoadings.size) // 引数は期待する値の変更の回数
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.initialize()

        viewModel.downloadSongData()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(100, viewModel.localDataVersion.value)
        assertEquals(true, viewModel.updateAvailable.value)
        assertEquals(-1, dataStore.actualInputVersion) // 変化なし
        assertEquals(
            "データに不整合があります。\n左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。",
            viewModel.errorMessage.value
        )
    }

    @Test
    fun ダウンロード実行_データ不整合でダウンロード失敗() = runTest {
        val service = TestSpreadSheetService().apply {
            newDataVersion = 999
            ssDataResult = FailureResult(listOf(Exception()))
        }

        val viewModel = DataUpdateViewModel(mockDatabase, service, dataStore)
        dataStore.expectedOutputVersion = 100

        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
        val isLoadingLatch = CountDownLatch(expectedIsLoadings.size) // 引数は期待する値の変更の回数
        val actualIsLoadings = mutableListOf<Boolean>()

        viewModel.isLoading.observeForever {
            actualIsLoadings.add(it)
            isLoadingLatch.countDown()
        }
        viewModel.initialize()

        viewModel.downloadSongData()

        assertEquals(expectedIsLoadings, actualIsLoadings)
        assertEquals(100, viewModel.localDataVersion.value)
        assertEquals(true, viewModel.updateAvailable.value)
        assertEquals(-1, dataStore.actualInputVersion) // 変化なし
        assertEquals(
            "データの取得に失敗しました。\nしばらく後に再実施していただくか、左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。",
            viewModel.errorMessage.value
        )
    }

//
//    @Test
//    fun checkNewVersionAvailable_available() = runTest {
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//        Assert.assertNull(viewModel.localDataVersion.value)
//
//        val intLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
//        val intExpectedValues = listOf(10) // 期待する値のリスト
//        val intActualValues = mutableListOf<Int>()
//
//        val intObserver = Observer<Int> {
//            intActualValues.add(it)
//            intLatch.countDown()
//        }
//        viewModel.localDataVersion.observeForever(intObserver)
//
//        viewModel.checkNewDataVersionAvailable()
//
//        Assert.assertTrue(viewModel.updateAvailable.value!!)
//        Assert.assertEquals(intExpectedValues, intActualValues)
//    }
//
//
//    @Test
//    fun checkNewVersionAvailable_unavailable() = runTest {
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//        Assert.assertNull(viewModel.localDataVersion.value)
//
//        val localVersionLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
//        val expectedLocalVersions = listOf(Int.MAX_VALUE) // 期待する値のリスト
//        val actualLocalVersions = mutableListOf<Int>()
//
//        viewModel.localDataVersion.observeForever {
//            actualLocalVersions.add(it)
//            localVersionLatch.countDown()
//        }
//
//        viewModel.checkNewDataVersionAvailable(Int.MAX_VALUE)
//
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//        Assert.assertEquals(expectedLocalVersions, actualLocalVersions)
//    }
//
//
//    @Test
//    fun downloadSongData() = runTest {
//        val expectedSourceVersion = 2023061121
//        viewModel.errorMessage.observeForever {
//            Assert.fail()
//        }
//
//        val localVersionLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
//        val expectedLocalVersions = listOf(expectedSourceVersion) // 期待する値のリスト
//        val actualLocalVersions = mutableListOf<Int>()
//
//        viewModel.localDataVersion.observeForever {
//            actualLocalVersions.add(it)
//            localVersionLatch.countDown()
//        }
//
//        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
//        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
//        val actualIsLoadings = mutableListOf<Boolean>()
//
//        viewModel.isLoading.observeForever {
//            actualIsLoadings.add(it)
//            isLoadingLatch.countDown()
//        }
//
//        viewModel.downloadSongData()
//        withContext(Dispatchers.IO) {
//            Assert.assertTrue(localVersionLatch.await(3, TimeUnit.SECONDS))
//            Assert.assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
//        }
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//        Assert.assertEquals(expectedLocalVersions, actualLocalVersions)
//        Assert.assertEquals(expectedIsLoadings, actualIsLoadings)
//        Assert.assertEquals(expectedSourceVersion, viewModel.sourceDataVersion)
//    }
//
//    @Test
//    fun downloadSongData_failToAccess() = runTest {
//        val viewModel =
//            EstimateByNameViewModel(mock(), TestSpreadSheetService("http://google.com"))
//
//        val errorMessageLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
//        val expectedErrorMessages = listOf(
//            "データの取得に失敗しました。\n" +
//                    "しばらく後に再実施していただくか、左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
//        ) // 期待する値のリスト
//        val actualErrorMessage = mutableListOf<String>()
//
//        viewModel.errorMessage.observeForever {
//            actualErrorMessage.add(it)
//            errorMessageLatch.countDown()
//        }
//
//        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
//        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
//        val actualIsLoadings = mutableListOf<Boolean>()
//
//        viewModel.isLoading.observeForever {
//            actualIsLoadings.add(it)
//            isLoadingLatch.countDown()
//        }
//
//        viewModel.downloadSongData()
//        withContext(Dispatchers.IO) {
//            Assert.assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
//        }
//        Assert.assertEquals(expectedIsLoadings, actualIsLoadings)
//        Assert.assertEquals(expectedErrorMessages, actualErrorMessage)
//        // REVISIT: 本来はinitで-1に設定されるべきだが非同期処理が間に合わないらしいので初期値で判定
//        Assert.assertEquals(0, viewModel.sourceDataVersion)
//        // init含め、一度もversionの値が書き換わらないのでデフォルトのfalse
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//    }
//
//    @Test
//    fun downloadSongData_InconsistentData() = runTest {
//        val viewModel =
//            EstimateByNameViewModel(
//                mock(), TestSpreadSheetService(
//                    "https://docs.google.com/spreadsheets/d/10BCNI4sarU2G32P7FLMchKa3IgQThNxFfr3E90L11cw/export?format=tsv&gid="
//                )
//            )
//
//        val errorMessageLatch = CountDownLatch(1) // 引数は期待する値の変更の回数
//        val expectedErrorMessages = listOf(
//            "データに不整合があります。\n" +
//                    "左上アイコンまたはTwitter(@sig_re)から開発にご連絡ください。"
//        ) // 期待する値のリスト
//        val actualErrorMessage = mutableListOf<String>()
//
//        viewModel.errorMessage.observeForever {
//            actualErrorMessage.add(it)
//            errorMessageLatch.countDown()
//        }
//
//        val isLoadingLatch = CountDownLatch(3) // 引数は期待する値の変更の回数
//        val expectedIsLoadings = listOf(false, true, false) // 期待する値のリスト
//        val actualIsLoadings = mutableListOf<Boolean>()
//
//        viewModel.isLoading.observeForever {
//            actualIsLoadings.add(it)
//            isLoadingLatch.countDown()
//        }
//
//        viewModel.downloadSongData()
//        withContext(Dispatchers.IO) {
//            Assert.assertTrue(isLoadingLatch.await(3, TimeUnit.SECONDS))
//        }
//        Assert.assertEquals(expectedIsLoadings, actualIsLoadings)
//        Assert.assertEquals(expectedErrorMessages, actualErrorMessage)
//        // REVISIT: 本来はinitで-1に設定されるべきだが非同期処理が間に合わないらしいので初期値で判定
//        Assert.assertEquals(0, viewModel.sourceDataVersion)
//        // init含め、一度もversionの値が書き換わらないのでデフォルトのfalse
//        Assert.assertFalse(viewModel.updateAvailable.value!!)
//    }
}

class TestSpreadSheetService : ISpreadSheetService {
    var newDataVersion: Int = -1
    var ssDataResult: SSDataResult = FailureResult(listOf())

    override suspend fun getNewDataVersion(): Int = newDataVersion

    override suspend fun createAllData(): SSDataResult = ssDataResult

    override fun getHttpClient(): HttpClient {
        TODO("Not yet implemented")
    }

    override fun getUrlBase(): String {
        TODO("Not yet implemented")
    }
}