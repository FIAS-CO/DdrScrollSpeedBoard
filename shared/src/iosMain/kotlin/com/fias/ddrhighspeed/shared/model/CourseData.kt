package com.fias.ddrhighspeed.shared.model

actual data class CourseData actual constructor(
    actual val id: Long,
    actual val name: String,
    actual val isDan: Boolean,
    actual val firstSongId: Long,
    actual val firstSongPropertyId: Long,
    actual val secondSongId: Long,
    actual val secondSongPropertyId: Long,
    actual val thirdSongId: Long,
    actual val thirdSongPropertyId: Long,
    actual val fourthSongId: Long,
    actual val fourthSongPropertyId: Long,
    actual val isDeleted: Boolean
) {
    actual fun getCourseLabel(): String {
        val prefix = if (isDan) "段位認定: " else "コース: "
        return prefix + name
    }
}