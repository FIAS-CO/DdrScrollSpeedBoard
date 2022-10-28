package com.example.ddrscrollspeedboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory

class ScrollSpeedBoardViewModel : ViewModel() {
    var scrollSpeed = MutableLiveData<String>()
    fun getScrollSpeedValue(): Int? = scrollSpeed.value?.toIntOrNull()

    // TODO resultRows が Listを返さないのわかりにくい
    val resultRows: () -> List<ResultRow> = {
        val scrollSpeedValue = getScrollSpeedValue() ?: 0
        ResultRowSetFactory().create(scrollSpeedValue)
    }

    fun setScrollSpeed(s: String) {
        // TODO 数字以外が入った場合や30未満・2001以上の際にwarningを出したい
        scrollSpeed.value = s
    }

    val countUp: () -> Unit = { countUpScrollSpeed() }

    // TODO 30未満2000以上の場合の動作(特に何もしない。範囲外はResultRowFactoryに任せる
    private fun countUpScrollSpeed() {
        var input = getScrollSpeedValue() ?: 29
        input++
        setScrollSpeed(input.toString())
    }

    val countDown: () -> Unit = { countDownScrollSpeed() }

    private fun countDownScrollSpeed() {
        var input = getScrollSpeedValue() ?: 31
        input--
        setScrollSpeed(input.toString())
    }
}