package com.fias.ddrhighspeed.search.songsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.Song

class EstimateByNameViewModel(private val db: Database) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    fun getNewSongs(): List<SongData> {
        return db.getNewSongs().map { convertToSongData(it) }
    }

    fun searchSongsByName(): List<SongData> {
        val word = searchWord.value ?: ""

        return if (word == "") getNewSongs()
        else db.searchSongsByName(word).map { convertToSongData(it) }
    }

    fun resetSearchWord() {
        searchWord.value = ""
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