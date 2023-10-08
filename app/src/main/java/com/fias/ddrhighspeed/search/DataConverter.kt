package com.fias.ddrhighspeed.search

import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.Course
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty

fun Song.convertToSongData(): SongData {
    with(this) {

        val values = listOfNotNull(base_bpm, sub_bpm, min_bpm, max_bpm).filter { it != 0.0 }

        val min = values.minOrNull()
        val max = values.maxOrNull()

        val actualDisplayBpm = when {
            min == null || max == null -> "No valid values."
            min == max -> "$min"
            else -> "$min ～ $max"
        }

        return SongData(
            id,
            name,
            composer,
            version,
            actualDisplayBpm,
            base_bpm,
            sub_bpm,
            min_bpm,
            max_bpm,
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

// TODO もしかしたら連結はDB任せにしたほうがいいかもしれない
// 複数箇所に連結処理があると問題だし、参照しているデータが足りてないし、あんまりいけてない
fun SongName.convertToSongData(prop: SongProperty): SongData {
    with(this) {

        val values = listOfNotNull(
            prop.base_bpm,
            prop.sub_bpm,
            prop.min_bpm,
            prop.max_bpm
        ).filter { it != 0.0 }

        val min = values.minOrNull()
        val max = values.maxOrNull()

        val actualDisplayBpm = when {
            min == null || max == null -> "No valid values."
            min == max -> "$min"
            else -> "$min ～ $max"
        }

        return SongData(
            id,
            name,
            prop.composer,
            version,
            actualDisplayBpm,
            prop.base_bpm,
            prop.sub_bpm,
            prop.min_bpm,
            prop.max_bpm,
            besp,
            bsp,
            dsp,
            esp,
            csp,
            bdp,
            ddp,
            edp,
            cdp,
            "", // REVISIT: 使用するなら入れるけど別テーブルが必要
            0, // REVISIT: 使用するなら入れるけど別テーブルが必要
            prop.difficulty_label ?: ""
        )
    }
}

fun Course.convertToCourseData(): CourseData {
    with(this) {
        return CourseData(
            id,
            name,
            is_dan == 1L,
            first_song_id,
            first_song_property_id, // 難易度指定がない場合は-1
            second_song_id,
            second_song_property_id, // 難易度指定がない場合は-1
            third_song_id,
            third_song_property_id, // 難易度指定がない場合は-1
            fourth_song_id,
            fourth_song_property_id, // 難易度指定がない場合は-1
            deleted == 1L
        )
    }
}
