package com.fias.ddrhighspeed.shared.model

expect class SongData(
    id: Long,
    name: String,
    composer: String?,
    version: String,
    displayBpm: String,
    baseBpm: Double,
    subBpm: Double?,
    minBpm: Double?,
    maxBpm: Double?,
    besp: Long,
    bsp: Long,
    dsp: Long,
    esp: Long,
    csp: Long,
    bdp: Long,
    ddp: Long,
    edp: Long,
    cdp: Long,
    shockArrow: String?,
    deleted: Long,
    difficultyLabel: String
) {
    val id: Long
    val name: String
    val composer: String?
    val version: String
    val displayBpm: String
    val baseBpm: Double
    val subBpm: Double?
    val minBpm: Double?
    val maxBpm: Double?
    val besp: Long
    val bsp: Long
    val dsp: Long
    val esp: Long
    val csp: Long
    val bdp: Long
    val ddp: Long
    val edp: Long
    val cdp: Long
    val shockArrow: String?
    val deleted: Long
    val difficultyLabel: String

    fun nameWithDifficultyLabel(): String
    fun hasHighSpeedArea(): Boolean
    fun hasLowSpeedArea(): Boolean
}