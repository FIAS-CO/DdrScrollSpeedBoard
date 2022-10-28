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
        scrollSpeedViewModel_input_正常系("400")
    }

    @Test
    fun scrollSpeedViewModel_input_30_最小値() {
        scrollSpeedViewModel_input_正常系("30")
    }

    @Test
    fun scrollSpeedViewModel_input_29_最小値未満() {
        scrollSpeedViewModel_input_正常系("29")
    }

    @Test
    fun scrollSpeedViewModel_input_2000_最大値() {
        scrollSpeedViewModel_input_正常系("2000")
    }

    @Test
    fun scrollSpeedViewModel_input_2001_最大値以上() {
        scrollSpeedViewModel_input_正常系("2001")
    }

    @Test
    fun scrollSpeedViewModel_input_マイナス1() {
        scrollSpeedViewModel_input_正常系("-1")
    }

    @Test
    fun scrollSpeedViewModel_input_小数点() {
        scrollSpeedViewModel_input_正常系("1.5")
    }

    @Test
    fun scrollSpeedViewModel_input_空文字() {
        scrollSpeedViewModel_input_正常系("")
    }

    @Test
    fun scrollSpeedViewModel_input_数字以外の文字列() {
        scrollSpeedViewModel_input_正常系("test")
    }

    @Test
    fun scrollSpeedViewModel_input_何も入れない() {
        assertThat(viewModel.scrollSpeed.value).isEqualTo(null)
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list_out_of_range)
    }

    @Test
    fun scrollSpeedViewModel_countUpScrollSpeed_正常系() {
        viewModel.setScrollSpeed("399")

        assertThat(viewModel.scrollSpeed.value).isEqualTo("399")
        viewModel.countUp()

        assertThat(viewModel.scrollSpeed.value).isEqualTo("400")
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list("400"))
    }

    @Test
    fun scrollSpeedViewModel_countUpScrollSpeed_空文字() {
        viewModel.setScrollSpeed("")

        assertThat(viewModel.scrollSpeed.value).isEqualTo("")
        viewModel.countUp()

        assertThat(viewModel.scrollSpeed.value).isEqualTo("30")
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list("30"))

    }

    @Test
    fun scrollSpeedViewModel_countDownScrollSpeed_正常系() {
        viewModel.setScrollSpeed("401")

        assertThat(viewModel.scrollSpeed.value).isEqualTo("401")
        viewModel.countDown()

        assertThat(viewModel.scrollSpeed.value).isEqualTo("400")
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list("400"))

    }

    @Test
    fun scrollSpeedViewModel_countDownScrollSpeed_空文字() {
        viewModel.setScrollSpeed("")

        assertThat(viewModel.scrollSpeed.value).isEqualTo("")
        viewModel.countDown()

        assertThat(viewModel.scrollSpeed.value).isEqualTo("30")
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list("30"))
    }

    private fun scrollSpeedViewModel_input_正常系(scrollSpeed: String) {
        viewModel.setScrollSpeed(scrollSpeed)

        assertThat(viewModel.scrollSpeed.value).isEqualTo(scrollSpeed)
        assertThat(viewModel.resultRows()).isEqualTo(ResultRowsDataSource.list(scrollSpeed))

        val value = viewModel.scrollSpeed.getOrAwaitValue()
        assertThat(value).isEqualTo(scrollSpeed)
    }
}