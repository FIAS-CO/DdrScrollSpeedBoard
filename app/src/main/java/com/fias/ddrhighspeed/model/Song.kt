package com.fias.ddrhighspeed.model

data class Song(
    val name :String,
    val composer :String,
    val version :String,
    val minBpm :Double,
    val freqBpm :Double, // 未設定の場合 -1.0
    val maxBpm :Double,
)
