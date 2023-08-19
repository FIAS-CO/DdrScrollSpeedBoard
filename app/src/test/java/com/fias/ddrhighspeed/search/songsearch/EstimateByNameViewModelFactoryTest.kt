package com.fias.ddrhighspeed.search.songsearch

import com.fias.ddrhighspeed.TestDatabase
import com.fias.ddrhighspeed.roughestimate.RoughEstimateViewModel
import com.fias.ddrhighspeed.shared.spreadsheet.SpreadSheetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class EstimateByNameViewModelFactoryTest {

    private val factory = EstimateByNameViewModelFactory(TestDatabase(), SpreadSheetService())

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

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