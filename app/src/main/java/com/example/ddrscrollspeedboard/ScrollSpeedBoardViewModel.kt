package com.example.ddrscrollspeedboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory

class ScrollSpeedBoardViewModel : ViewModel() {
    // TODO 変数の扱いはこれでいいのか
    private var _scrollSpeed = MutableLiveData<String>()
    val scrollSpeed = _scrollSpeed

    // TODO resultRows が Listを返さないのわかりにくい
    val resultRows: () -> List<ResultRow> = {
        val resultRows = _scrollSpeed.value?.toIntOrNull()?.let { ResultRowSetFactory().create(it) }
        if (!resultRows.isNullOrEmpty()) resultRows else listOf()
    }

    fun setScrollSpeed(s: String) {
        // TODO 数字以外が入った場合
        _scrollSpeed.value = s
    }

    val countUp: () -> Unit = { countUpScrollSpeed() }

    private fun countUpScrollSpeed() {
        var input = _scrollSpeed.value?.toIntOrNull() ?: 1
        input++
        setScrollSpeed(input.toString())
    }

    val countDown: () -> Unit = { countDownScrollSpeed() }

    private fun countDownScrollSpeed() {
        var input = scrollSpeed.value?.toIntOrNull() ?: 30
        input = if (input <= 30) 30 else --input
        setScrollSpeed(input.toString())
    }

    init {
        setScrollSpeed("400") // 初回起動時設定
    }
}