package com.fias.ddrhighspeed.shared.model

data class ResultRowForCourseDetail(
    val songName: String,
    val bpm: String,
    val suggestedHighSpeed: Double,
    val difficulty: String?
) : Comparable<ResultRowForCourseDetail> {
    override fun compareTo(other: ResultRowForCourseDetail): Int {
        return bpm.toDouble().compareTo(other.bpm.toDouble())
    }
}