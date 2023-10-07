package com.fias.ddrhighspeed.search.songdetail.movie

import androidx.compose.ui.graphics.Color
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Movie
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SongMovieViewModelTest {

    private var mockDatabase = mock<IDatabase>()
    private val mockSongs = listOf(
        Movie(1,1, "DSP", "Youtube", "movie3", "movie3"),
        Movie(11, 11, "BDP", "Youtube", "movie6", "movie6"),
        Movie(111,111, "BESP", "Youtube", "movie1", "movie1"),
        Movie(21, 21, "DDP", "Youtube", "movie7", "movie7"),
        Movie(221, 221, "CDP", "Youtube", "movie9", "movie9"),
        Movie(2221, 2221, "error", "Youtube", "movie0", "movie0"),
        Movie(331,331, "ESP", "Youtube", "movie4", "movie4"),
        Movie(3331,3331, "EDP", "Youtube", "movie8", "movie8"),
        Movie(31, 31, "BSP", "Youtube", "movie2", "movie2"),
        Movie(41, 41, "CSP", "Youtube", "movie5", "movie5"),
    )
    private val emptyMockSongs = listOf<Movie>()

    private val cyan = Color(0f, 0.79f, 0.79f, 1.0f)
    private val orange = Color(0.98f, 0.63f, 0f, 1.0f)
    private val red = Color(1f, 0f, 0f, 0.75f)
    private val purple = Color(0.5f, 0f, 0.5f, 1.0f)
    private val green = Color(0f, 0.5f, 0f, 1.0f)
    private val gray = Color(0.5f, 0.5f, 0.5f, 1.0f)

    @Before
    fun setUp() {
        whenever(mockDatabase.getMovies(1)).thenReturn(mockSongs)
        whenever(mockDatabase.getMovies(99999)).thenReturn(emptyMockSongs)
    }

    @Test
    fun getMovieList() {
        val movieList = SongMovieViewModel(mockDatabase, 1).movieList

        assertEquals(10, movieList.size)
        val expected = listOf(
            MovieModel("Single Beginner", cyan, "movie1"),
            MovieModel("Single Basic", orange, "movie2"),
            MovieModel("Single Difficult", red, "movie3"),
            MovieModel("Single Expert", green, "movie4"),
            MovieModel("Single Challenge", purple, "movie5"),
            MovieModel("Double Basic", orange, "movie6"),
            MovieModel("Double Difficult", red, "movie7"),
            MovieModel("Double Expert", green, "movie8"),
            MovieModel("Double Challenge", purple, "movie9"),
            MovieModel("Undefined Difficulty", gray, "movie0"),
        )
        assertEquals(expected, movieList)
    }

    @Test
    fun getMovieList_No_Movie() {
        val movieList = SongMovieViewModel(mockDatabase, 99999).movieList

        assertEquals(0, movieList.size)
    }
}