package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Long,
    val name: String,
    val firstSongId: Long,
    val secondSongId: Long,
    val thirdSongId: Long,
    val fourthSongId: Long,
) : Parcelable