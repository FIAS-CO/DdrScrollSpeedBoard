package com.example.ddrscrollspeedboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory

class ScrollSpeedBoardViewModel : ViewModel() {
    // TODO 変数の扱いはこれでいいのか
    // TODO getter は .value を取るよう検討
    var scrollSpeed = MutableLiveData<String>()

    // TODO resultRows が Listを返さないのわかりにくい
    val resultRows: () -> List<ResultRow> = {
        val scrollSpeedValue = scrollSpeed.value?.toIntOrNull() ?: 0
        ResultRowSetFactory().create(scrollSpeedValue)
    }

    fun setScrollSpeed(s: String) {
        // TODO 数字以外が入った場合や30未満・2001以上の際にwarningを出したい
        scrollSpeed.value = s
    }

    val countUp: () -> Unit = { countUpScrollSpeed() }

    // TODO 30未満2000以上の場合の動作(特に何もしない。範囲外はResultRowFactoryに任せる
    private fun countUpScrollSpeed() {
        var input = scrollSpeed.value?.toIntOrNull() ?: 29
        input++
        setScrollSpeed(input.toString())
    }

    val countDown: () -> Unit = { countDownScrollSpeed() }

    private fun countDownScrollSpeed() {
        var input = scrollSpeed.value?.toIntOrNull() ?: 31
        input--
        setScrollSpeed(input.toString())
    }
}