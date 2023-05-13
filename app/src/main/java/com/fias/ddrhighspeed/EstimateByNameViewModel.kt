package com.fias.ddrhighspeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.Song

class EstimateByNameViewModel(private val db: Database) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    fun getNewSongs(): List<Song> {
        return db.getNewSongs()
    }

    fun searchSongsByName(searchWord: String): List<Song> {
        return if (searchWord == "") getNewSongs()
        else db.searchSongsByName(searchWord)
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