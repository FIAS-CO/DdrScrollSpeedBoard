package com.fias.ddrhighspeed.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Songs")
data class Song(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "composer") val composer: String?,
    @ColumnInfo(name = "version") val version: String,
    @ColumnInfo(name = "display_bpm") val displayBpm: String,
    @ColumnInfo(name = "min_bpm") val minBpm: Double?,
    @ColumnInfo(name = "max_bpm") val maxBpm: Double?,
    @ColumnInfo(name = "base_bpm") val baseBpm: Double?,
    @ColumnInfo(name = "sub_bpm") val subBpm: Double?,
    @ColumnInfo(name = "besp") val besp: Int?,
    @ColumnInfo(name = "bsp") val bsp: Int?,
    @ColumnInfo(name = "dsp") val dsp: Int?,
    @ColumnInfo(name = "esp") val esp: Int?,
    @ColumnInfo(name = "csp") val csp: Int?,
    @ColumnInfo(name = "bdp") val bdp: Int?,
    @ColumnInfo(name = "ddp") val ddp: Int?,
    @ColumnInfo(name = "edp") val edp: Int?,
    @ColumnInfo(name = "cdp") val cdp: Int?,
    @ColumnInfo(name = "shock_arrow") val ShockArrow: String?,
    @ColumnInfo(name = "deleted") val Deleted: Int?, // 削除は1, それ以外は null
)
