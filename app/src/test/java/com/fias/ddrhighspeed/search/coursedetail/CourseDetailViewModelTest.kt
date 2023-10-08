package com.fias.ddrhighspeed.search.coursedetail

import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.robolectric.RobolectricTestRunner

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(RobolectricTestRunner::class)
internal class CourseDetailViewModelTest {
    private lateinit var viewModel: CourseDetailViewModel
    private val courseData = CourseData(
        1, "Test Course 1", true,
        1, -1,
        2, -1,
        3, -1,
        4, -1,
        true
    )

    private val courseDataWithSongProp = CourseData(
        1, "Test Course 1", true,
        1, 11,
        2, 22,
        3, 33,
        4, 44,
        true
    )

    @Before
    fun setUp() {
        val mockDatabase = mock<IDatabase>()

        val mockSongs = listOf(
            Song(
                1, "Test Song 1", "composer1", "A3", "123",
                null, null, 100.0, null,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "", 0, null
            ),
            Song(
                2, "Test Song 2", "composer2", "A3", "234-567",
                123.0, 234.0, 200.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
            ),
            Song(
                3, "Test Song 3", "composer3", "A3", "234-567",
                123.0, 234.0, 300.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
            ),
            Song(
                4, "Test Song 4", "composer4", "A3", "234-567",
                123.0, 234.0, 400.0, 578.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
            ),
        )

        val mockSongNames = listOf(
            SongName(
                1, "Test Song 1", "Test Song 1 ruby", "A3", 123,
                11,
                1, 2, 3, 4, 5, 6, 7, 8, 9
            ),
            SongName(
                2, "Test Song 2", "Test Song 2 ruby", "A3", 123,
                11,
                1, 2, 3, 4, 5, 6, 7, 8, 9
            ),
            SongName(
                3, "Test Song 3", "Test Song 3 ruby", "A3", 123,
                11,
                1, 2, 3, 4, 5, 6, 7, 8, 9
            ),
            SongName(
                4, "Test Song 4", "Test Song 4 ruby", "A3", 123,
                11,
                1, 2, 3, 4, 5, 6, 7, 8, 9
            )
        )

        val mockSongProps = listOf(
            SongProperty(
                11, 1, "for song property1", 100.0,
                null, null, null, null,
            ),
            SongProperty(
                22, 2, "for song property2",
                200.0, 578.0, 123.0, 234.0,
                "DSP,ESP"
            ),
            SongProperty(
                33, 3, "for song property3",
                300.0, 578.0, 123.0, 234.0,
                "DSP,ESP"
            ),
            SongProperty(
                44, 4, "for song property4",
                400.0, 578.0, 123.0, 234.0,
                "DSP,ESP"
            )
        )

        val longCaptor = ArgumentCaptor.forClass(Long::class.java)

        whenever(mockDatabase.getSongsById(longCaptor.capture())).thenAnswer {
            mockSongs.filter { it.id == longCaptor.value }
        }
        whenever(mockDatabase.getSongNameById(longCaptor.capture())).thenAnswer {
            mockSongNames.first { it.id == longCaptor.value }
        }
        whenever(mockDatabase.getSongPropertyById(longCaptor.capture())).thenAnswer {
            mockSongProps.first { it.id == longCaptor.value }
        }

        viewModel = CourseDetailViewModel(mockDatabase)
    }

    @Test
    fun コース設定() {
        viewModel.course = courseData
        val songData1 = SongData(
            1, "Test Song 1", "composer1", "A3", "100.0",
            100.0, null, null, null,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "", 0, ""
        )
        val songData2 = SongData(
            2, "Test Song 2", "composer2", "A3", "123.0 ～ 578.0",
            200.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "DSP,ESP", 1, "(Test)"
        )
        val songData3 = SongData(
            3, "Test Song 3", "composer3", "A3", "123.0 ～ 578.0",
            300.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "DSP,ESP", 1, "(Test)"
        )
        val songData4 = SongData(
            4, "Test Song 4", "composer4", "A3", "123.0 ～ 578.0",
            400.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "DSP,ESP", 1, "(Test)"
        )

        Assert.assertEquals(songData1, viewModel.firstSong)
        Assert.assertEquals(songData2, viewModel.secondSong)
        Assert.assertEquals(songData3, viewModel.thirdSong)
        Assert.assertEquals(songData4, viewModel.fourthSong)
    }

    @Test
    fun コース設定_SongPropertyが設定されている場合() {
        viewModel.course = courseDataWithSongProp
        val songData1 = SongData(
            1, "Test Song 1", "for song property1", "A3", "100.0",
            100.0, null, null, null,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "", 0, ""
        )
        val songData2 = SongData(
            2, "Test Song 2", "for song property2", "A3", "123.0 ～ 578.0",
            200.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "", 0, "DSP,ESP"
        )
        val songData3 = SongData(
            3, "Test Song 3", "for song property3", "A3", "123.0 ～ 578.0",
            300.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "", 0, "DSP,ESP"
        )
        val songData4 = SongData(
            4, "Test Song 4", "for song property4", "A3", "123.0 ～ 578.0",
            400.0, 578.0, 123.0, 234.0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            "", 0, "DSP,ESP"
        )

        Assert.assertEquals(songData1, viewModel.firstSong)
        Assert.assertEquals(songData2, viewModel.secondSong)
        Assert.assertEquals(songData3, viewModel.thirdSong)
        Assert.assertEquals(songData4, viewModel.fourthSong)
    }

    @Test
    fun calculate_1() {
        viewModel.course = courseData
        val actual = viewModel.calculate(600, 1)

        Assert.assertEquals("", 6.0, actual, 0.0)
    }

    @Test
    fun calculate_2() {
        viewModel.course = courseData
        val actual = viewModel.calculate(600, 2)

        Assert.assertEquals("", 3.0, actual, 0.0)
    }

    @Test
    fun calculate_3() {
        viewModel.course = courseData
        val actual = viewModel.calculate(600, 3)

        Assert.assertEquals("", 2.0, actual, 0.0)
    }

    @Test
    fun calculate_4() {
        viewModel.course = courseData
        val actual = viewModel.calculate(600, 4)

        Assert.assertEquals("", 1.5, actual, 0.0)
    }

    @Test
    fun calculate_0() {
        viewModel.course = courseData
        assertThrows(IndexOutOfBoundsException::class.java) {
            viewModel.calculate(600, 0)
        }
    }

    @Test
    fun calculate_5() {
        viewModel.course = courseData
        assertThrows(IndexOutOfBoundsException::class.java) {
            viewModel.calculate(600, 5)
        }
    }
}