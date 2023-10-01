package com.fias.ddrhighspeed.search

import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.Course
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
