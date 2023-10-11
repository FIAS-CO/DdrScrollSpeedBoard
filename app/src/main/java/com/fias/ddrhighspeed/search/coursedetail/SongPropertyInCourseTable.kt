package com.fias.ddrhighspeed.search.coursedetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.fias.ddrhighspeed.R

class SongPropertyInCourseTable @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val songNameText: TextView
    private val leftText: TextView
    private val bottomRightText: TextView
    private val bottomCenterText: TextView
    private val bottomLeftText: TextView
    private val button: AppCompatButton

    init {
        LayoutInflater.from(context).inflate(R.layout.song_property_in_course_table, this, true)

        songNameText = findViewById(R.id.song_label)
        leftText = findViewById(R.id.left_text)
        bottomRightText = findViewById(R.id.bottom_right_text)
        bottomCenterText = findViewById(R.id.bottom_center_text)
        bottomLeftText = findViewById(R.id.bottom_left_text)
        button = findViewById(R.id.reset_button)
    }

    fun setSongLabel(text: String) {
        songNameText.text = text
    }

    fun setLeftText(text: String) {
        leftText.text = text
    }

    fun setBottomRightText(text: String) {
        bottomRightText.text = text
    }

    fun setBottomCenterText(text: String) {
        bottomCenterText.text = text
    }

    fun setBottomLeftText(text: String) {
        bottomLeftText.text = text
    }

    fun setButtonOnClick(action: () -> Unit) {
        button.setOnClickListener {
            action.invoke()
        }
    }
}