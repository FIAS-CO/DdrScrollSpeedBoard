package com.fias.ddrhighspeed.search.songdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class SongDetailViewModel(
    private val db: IDatabase
) : ViewModel() {
    var songId: Long = -1
        set(value) {
            field = value
            song = convertToSongData(db.getSongsById(songId).first())
        }

    private val rsFactory = ResultRowSetFactory()

    lateinit var song: SongData
    val songName: String
        get() = song.name

    private fun convertToSongData(song: Song): SongData {
        with(song) {
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
                deleted
            )
        }
    }

    fun createRows(scrollSpeedValue: Int?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        val list = mutableListOf<ResultRowForDetail>()
        song.apply {
            max_bpm?.let {
                list.add(rsFactory.createForDetail(value, "最大", it))
            }
            min_bpm?.let {
                list.add(rsFactory.createForDetail(value, "最小", it))
            }
            base_bpm?.let {
                list.add(rsFactory.createForDetail(value, "基本①", it))
            }
            sub_bpm?.let {
                list.add(rsFactory.createForDetail(value, "基本②", it))
            }
            list.removeIf { it.bpm == "0.0" }
            list.sortDescending() // BPM でソート
        }
        return list
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class SongDetailViewModelFactory(
    private val db: IDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SongDetailViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}