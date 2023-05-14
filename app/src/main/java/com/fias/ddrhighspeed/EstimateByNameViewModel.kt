package com.fias.ddrhighspeed

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.Song
import kotlinx.parcelize.Parcelize

class EstimateByNameViewModel(private val db: Database) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    fun getNewSongs(): List<SongData> {
        return db.getNewSongs().map { convertToSongData(it) }
    }

    fun searchSongsByName(searchWord: String): List<SongData> {
        return if (searchWord == "") getNewSongs()
        else db.searchSongsByName(searchWord).map { convertToSongData(it) }
    }

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
}

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
) :Parcelable

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateByNameViewModelFactory(private val db: Database) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateByNameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EstimateByNameViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}