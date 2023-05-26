package com.fias.ddrhighspeed.search.songsearch

import com.fias.ddrhighspeed.roughestimate.RoughEstimateViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class EstimateByNameViewModelFactoryTest {

    private val factory = EstimateByNameViewModelFactory(mock())

    @Test
    fun `create returns EstimateByNameViewModel instance for EstimateByNameViewModel class`() {
        val viewModel = factory.create(EstimateByNameViewModel::class.java)

        @Suppress("USELESS_IS_CHECK")
        assertTrue(viewModel is EstimateByNameViewModel)
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