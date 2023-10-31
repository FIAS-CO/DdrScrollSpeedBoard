package com.fias.ddrhighspeed

import kotlinx.serialization.Serializable

// TODO Android側にあるものをこちらに置き換える
@Serializable
data class SongData(
    val id: Long,
    val name: String,
    val composer: String?,
    val version: String,
    val displayBpm: String,
    val baseBpm: Double,
    val subBpm: Double?,
    val minBpm: Double?,
    val maxBpm: Double?,
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
) {
    fun nameWithDifficultyLabel(): String =
        name + if (difficultyLabel.isNotEmpty()) "(${difficultyLabel})" else ""

    fun hasHighSpeedArea() = hasMaxBpm() || hasSubBpm()
    fun hasLowSpeedArea() = hasValue(minBpm)

    private fun hasMaxBpm() = hasValue(maxBpm)
    private fun hasSubBpm() = hasValue(subBpm)

    private fun hasValue(value: Double?) = value?.let { it > 0.0 } ?: false

}
