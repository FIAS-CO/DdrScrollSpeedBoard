package com.fias.ddrhighspeed.shared.model

expect class CourseData(
    id: Long = -1,
    name: String = "",
    isDan: Boolean = false,
    firstSongId: Long = -1,
    firstSongPropertyId: Long = -1,
    secondSongId: Long = -1,
    secondSongPropertyId: Long = -1,
    thirdSongId: Long = -1,
    thirdSongPropertyId: Long = -1,
    fourthSongId: Long = -1,
    fourthSongPropertyId: Long = -1,
    isDeleted: Boolean = true
) {
    val id: Long
    val name: String
    val isDan: Boolean
    val firstSongId: Long
    val firstSongPropertyId: Long
    val secondSongId: Long
    val secondSongPropertyId: Long
    val thirdSongId: Long
    val thirdSongPropertyId: Long
    val fourthSongId: Long
    val fourthSongPropertyId: Long
    val isDeleted: Boolean

    fun getCourseLabel(): String
}