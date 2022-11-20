package com.fias.ddrhighspeed

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

class TextEditWithHideKeyBoard(context: Context, attrs: AttributeSet?) :
    TextInputEditText(context, attrs) {

    init {
        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideSoftwareKeyboard(context)
            }
        }
    }

    private fun hideSoftwareKeyboard(context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}