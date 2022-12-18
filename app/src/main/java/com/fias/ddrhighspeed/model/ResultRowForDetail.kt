package com.fias.ddrhighspeed.model

data class ResultRowForDetail(
    val category: String,
    val bpm: String,
    val highSpeed: String,
    val scrollSpeed: String
) : Comparable<ResultRowForDetail> {
    override fun compareTo(other: ResultRowForDetail): Int {
        return bpm.toDouble().compareTo(other.bpm.toDouble())
    }
}
