package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Long,
    val name: String
) : Parcelable