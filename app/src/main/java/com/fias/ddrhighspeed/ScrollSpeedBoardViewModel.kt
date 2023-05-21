package com.fias.ddrhighspeed

import android.os.Handler
import android.os.Looper
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

    private val handler = Handler(Looper.getMainLooper())

    fun longPushButtonCommand(action: () -> Unit) {
        val scrollSpeed = getScrollSpeedValue()

        // スピンボタン長押し時に処理が連続実行されないように
        handler.postDelayed({
            if (scrollSpeed == getScrollSpeedValue()) {
                action()
            }
        }, 200)
    }

    class NewMutableLiveData<T> : MutableLiveData<T>() {
        override fun setValue(value: T) {
            if (this.value == value) return
            super.setValue(value)
        }
    }
}