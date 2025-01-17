package com.fias.ddrhighspeed.search.songsearch

import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EstimateByNameViewModelTest {
    private lateinit var viewModel: EstimateByNameViewModel

    @Before
    fun setUp() {
        val mockDatabase = mock<IDatabase>()

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
            ),
        )
        whenever(mockDatabase.getNewSongs()).thenReturn(mockSongs)

        viewModel = EstimateByNameViewModel(mockDatabase)
    }

    @Test
    fun searchSongsByName_returns_correct_list_when_searchWord_is_not_empty() {
        viewModel.searchWord.value = "Song 2"

        // When
        val result = viewModel.searchSongsByName()

        val expected = listOf(
            SongData(
                2, "Test Song 2", "composer2", "DDR", "123.0 ～ 578.0",
                345.0, 578.0, 123.0, 234.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
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
                1, "Test Song 1", "composer1", "A3", "123.0",
                123.0, null, null, null,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "", 0, ""
            ),
            SongData(
                2, "Test Song 2", "composer2", "DDR", "123.0 ～ 578.0",
                345.0, 578.0, 123.0, 234.0,
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                "DSP,ESP", 1, "(Test)"
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
}