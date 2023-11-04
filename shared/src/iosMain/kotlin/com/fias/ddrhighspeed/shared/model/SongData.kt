package com.fias.ddrhighspeed.shared.model

actual data class SongData actual constructor(
    actual val id: Long,
    actual val name: String,
    actual val composer: String?,
    actual val version: String,
    actual val displayBpm: String,
    actual val baseBpm: Double,
    actual val subBpm: Double?,
    actual val minBpm: Double?,
    actual val maxBpm: Double?,
    actual val besp: Long,
    actual val bsp: Long,
    actual val dsp: Long,
    actual val esp: Long,
    actual val csp: Long,
    actual val bdp: Long,
    actual val ddp: Long,
    actual val edp: Long,
    actual val cdp: Long,
    actual val shockArrow: String?,
    actual val deleted: Long,
    actual val difficultyLabel: String
) {
    actual fun nameWithDifficultyLabel(): String =
        name + if (difficultyLabel.isNotEmpty()) "(${difficultyLabel})" else ""

    actual fun hasHighSpeedArea() = hasMaxBpm() || hasSubBpm()
    actual fun hasLowSpeedArea() = hasValue(minBpm)

    private fun hasMaxBpm() = hasValue(maxBpm)
    private fun hasSubBpm() = hasValue(subBpm)

    private fun hasValue(value: Double?) = value?.let { it > 0.0 } ?: false

}