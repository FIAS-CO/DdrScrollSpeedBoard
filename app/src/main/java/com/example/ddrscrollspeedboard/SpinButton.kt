package com.example.ddrscrollspeedboard

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class SpinButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setSpinButtonListener(action: () -> Unit) {
        val spinButtonListener = SpinButtonListener(action)
        setOnClickListener(spinButtonListener)
        setOnLongClickListener(spinButtonListener)
        setOnTouchListener(spinButtonListener)
    }
}