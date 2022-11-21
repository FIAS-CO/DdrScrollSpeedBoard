package com.fias.ddrhighspeed.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class SpinButton(context: Context, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    fun setSpinButtonListener(action: () -> Unit) {
        val spinButtonListener = SpinButtonListener(action)
        setOnClickListener(spinButtonListener)
        setOnLongClickListener(spinButtonListener)
        setOnTouchListener(spinButtonListener)
    }
}