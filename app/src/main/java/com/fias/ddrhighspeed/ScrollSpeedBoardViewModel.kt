package com.fias.ddrhighspeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.model.ResultRow
import com.fias.ddrhighspeed.model.ResultRowSetFactory

class ScrollSpeedBoardViewModel : ViewModel() {
    val scrollSpeed = NewMutableLiveData<String>()

    fun getScrollSpeedValue(): Int? = scrollSpeed.value?.toIntOrNull()

    // TODO resultRows が Listを返さないのわかりにくい
    val resultRows: () -> List<ResultRow> = {
        val scrollSpeedValue = getScrollSpeedValue() ?: 0
        ResultRowSetFactory().create(scrollSpeedValue)
    }

    val countUp: () -> Unit = { countUpScrollSpeed() }

    private fun countUpScrollSpeed() {
        var input = getScrollSpeedValue() ?: 29
        input++
        scrollSpeed.setValue(input.toString())
    }

    val countDown: () -> Unit = { countDownScrollSpeed() }

    private fun countDownScrollSpeed() {
        var input = getScrollSpeedValue() ?: 31
        input--
        scrollSpeed.setValue(input.toString())
    }

    // TODO 別のViewModelに。使う範囲が違うので
    val searchWord = NewMutableLiveData<String>()

    class NewMutableLiveData<T> : MutableLiveData<T>() {
        override fun setValue(value: T) {
            if (this.value == value) return
            super.setValue(value)
        }
    }
}