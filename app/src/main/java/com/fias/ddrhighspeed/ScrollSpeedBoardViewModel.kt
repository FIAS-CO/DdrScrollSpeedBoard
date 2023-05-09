package com.fias.ddrhighspeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScrollSpeedBoardViewModel : ViewModel() {
    val scrollSpeed = NewMutableLiveData<String>()

    fun getScrollSpeedValue(): Int? = scrollSpeed.value?.toIntOrNull()

    val countUp: () -> Unit = {
        var input = getScrollSpeedValue() ?: 29
        input++
        scrollSpeed.setValue(input.toString())
    }

    val countDown: () -> Unit = {
        var input = getScrollSpeedValue() ?: 31
        input--
        scrollSpeed.setValue(input.toString())
    }

    class NewMutableLiveData<T> : MutableLiveData<T>() {
        override fun setValue(value: T) {
            if (this.value == value) return
            super.setValue(value)
        }
    }
}