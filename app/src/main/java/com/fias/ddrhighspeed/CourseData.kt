package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Long,
    val name: String,
    val isDan: Boolean,
    val firstSongId: Long,
    val firstSongPropertyId: Long,
    val secondSongId: Long,
    val secondSongPropertyId: Long,
    val thirdSongId: Long,
    val thirdSongPropertyId: Long,
    val fourthSongId: Long,
    val fourthSongPropertyId: Long,
    val isDeleted: Boolean
) : Parcelable