package com.example.ddrscrollspeedboard

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ddrscrollspeedboard.data.ResultRowsDataSource
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("NonAsciiCharacters", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class ScrollSpeedBoardViewModelTest {
    private lateinit var viewModel: ScrollSpeedBoardViewModel

    @Before
    fun setUp() {
        viewModel = ScrollSpeedBoardViewModel()
    }

    @Test
    fun scrollSpeedViewModel_input_400() {
        viewModel.setScrollSpeed("400")

        assertThat(viewModel.scrollSpeed.value).isEqualTo("400")
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list_400)

        val value = viewModel.scrollSpeed.getOrAwaitValue()
        assertThat(value).isEqualTo("400")
    }

    @Test
    fun scrollSpeedViewModel_input_30_最小値() {

    }

    @Test
    fun scrollSpeedViewModel_input_29_最小値未満() {

    }

    @Test
    fun scrollSpeedViewModel_input_2000_最大値() {

    }

    @Test
    fun scrollSpeedViewModel_input_マイナス1() {

    }

    @Test
    fun scrollSpeedViewModel_input_2001_最大値以上() {

    }

    @Test
    fun scrollSpeedViewModel_input_空文字() {

    }

    @Test
    fun scrollSpeedViewModel_input_数字以外の文字列() {

    }

    @Test
    fun scrollSpeedViewModel_input_null() {

    }
}