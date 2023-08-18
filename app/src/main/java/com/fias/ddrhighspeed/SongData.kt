package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongData(
    val id: Long,
    val name: String,
    val composer: String?,
    val version: String,
    val displayBpm: String,
    val minBpm: Double?,
    val maxBpm: Double?,
    val baseBpm: Double,
    val subBpm: Double?,
    val besp: Long,
    val bsp: Long,
    val dsp: Long,
    val esp: Long,
    val csp: Long,
    val bdp: Long,
    val ddp: Long,
    val edp: Long,
    val cdp: Long,
    val shockArrow: String?,
    val deleted: Long,
    val difficultyLabel: String
) : Parcelable {
    fun nameWithDifficultyLabel(): String =
        name + if (difficultyLabel.isNotEmpty()) "(${difficultyLabel})" else ""
}
