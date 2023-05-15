package com.fias.ddrhighspeed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongData(
    val id: Long,
    val name: String,
    val composer: String?,
    val version: String,
    val display_bpm: String,
    val min_bpm: Double?,
    val max_bpm: Double?,
    val base_bpm: Double?,
    val sub_bpm: Double?,
    val besp: Long?,
    val bsp: Long?,
    val dsp: Long?,
    val esp: Long?,
    val csp: Long?,
    val bdp: Long?,
    val ddp: Long?,
    val edp: Long?,
    val cdp: Long?,
    val shock_arrow: String?,
    val deleted: Long?
) : Parcelable
