package com.example.ddrscrollspeedboard

import androidx.lifecycle.ViewModel
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory


class ScrollSpeedBoardViewModel : ViewModel() {
    private var _scrollSpeed = "400"
    val scrollSpeed: String
        get() = _scrollSpeed

    val resultRows: () -> List<ResultRow> = {
        val resultRows = _scrollSpeed.toIntOrNull()?.let { ResultRowSetFactory().create(it) }
        if (!resultRows.isNullOrEmpty()) resultRows else listOf()
    }

    fun setScrollSpeed(scrollSpeed: String) {
        _scrollSpeed = scrollSpeed
    }

}