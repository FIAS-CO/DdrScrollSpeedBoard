package com.fias.ddrhighspeed.search.songdetail.movie

import com.fias.ddrhighspeed.roughestimate.RoughEstimateViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class SongMovieViewModelFactoryTest {

    private val factory = SongMovieViewModelFactory(mock(), 999999)

    @Test
    fun `create returns SongMovieViewModel instance for SongMovieViewModel class`() {
        val viewModel = factory.create(SongMovieViewModel::class.java)

        @Suppress("USELESS_IS_CHECK")
        assertTrue(viewModel is SongMovieViewModel)
    }

    @Test
    fun `create throws IllegalArgumentException for wrong class`() {
        try {
            factory.create(RoughEstimateViewModel::class.java)
            fail("Expected an IllegalArgumentException to be thrown")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Unknown ViewModel class"))
        }
    }
}