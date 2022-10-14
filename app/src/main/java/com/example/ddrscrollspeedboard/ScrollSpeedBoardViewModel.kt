package com.example.ddrscrollspeedboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory

class ScrollSpeedBoardViewModel : ViewModel() {
    var scrollSpeed = MutableLiveData<String>()

    val resultRows: () -> List<ResultRow> = {
        val resultRows = scrollSpeed.value?.toIntOrNull()?.let { ResultRowSetFactory().create(it) }
        if (!resultRows.isNullOrEmpty()) resultRows else listOf()
    }

    fun setScrollSpeed(s: String) {
        scrollSpeed.value = s
    }

}