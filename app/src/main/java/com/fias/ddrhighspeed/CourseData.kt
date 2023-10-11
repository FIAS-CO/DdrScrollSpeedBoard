package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourseData(
    val id: Long = -1,
    val name: String = "",
    val isDan: Boolean = false,
    val firstSongId: Long = -1,
    val firstSongPropertyId: Long = -1,
    val secondSongId: Long = -1,
    val secondSongPropertyId: Long = -1,
    val thirdSongId: Long = -1,
    val thirdSongPropertyId: Long = -1,
    val fourthSongId: Long = -1,
    val fourthSongPropertyId: Long = -1,
    val isDeleted: Boolean = true
) : Parcelable {
    fun getCourseLabel(): String {
        val prefix = if (isDan) "段位認定: " else "コース: "
        return prefix + name
    }
}