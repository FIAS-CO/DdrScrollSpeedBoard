package com.fias.ddrhighspeed.search

import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.Song

fun Song.convertToSongData(): SongData {
    with(this) {
        return SongData(
            id,
            name,
            composer,
            version,
            display_bpm,
            min_bpm,
            max_bpm,
            base_bpm,
            sub_bpm,
            besp,
            bsp,
            dsp,
            esp,
            csp,
            bdp,
            ddp,
            edp,
            cdp,
            shock_arrow,
            deleted,
            difficulty_label ?: ""
        )
    }
}
