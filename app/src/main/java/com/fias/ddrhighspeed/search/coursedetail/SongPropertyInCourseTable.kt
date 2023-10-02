package com.fias.ddrhighspeed.search.coursedetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.fias.ddrhighspeed.R

class SongPropertyInCourseTable @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val songNameText: TextView
    private val leftText: TextView
    private val topRightText: TextView
    private val bottomRightText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.song_property_in_course_table, this, true)

        songNameText = findViewById(R.id.song_label)
        leftText = findViewById(R.id.left_text)
        topRightText = findViewById(R.id.top_right_text)
        bottomRightText = findViewById(R.id.bottom_right_text)
    }

    fun setSongLabel(text: String) {
        songNameText.text = text
    }

    fun setLeftText(text: String) {
        leftText.text = text
    }

    fun setTopRightText(text: String) {
        topRightText.text = text
    }

    fun setBottomRightText(text: String) {
        bottomRightText.text = text
    }
}