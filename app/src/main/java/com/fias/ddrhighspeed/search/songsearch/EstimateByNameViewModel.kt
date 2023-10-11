package com.fias.ddrhighspeed.search.songsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.search.convertToSongData
import com.fias.ddrhighspeed.shared.cache.IDatabase

// TODO:テストを足す
class EstimateByNameViewModel(
    private val db: IDatabase
) : ViewModel() {
    val searchWord = MutableLiveData<String>()

    val baseSongDataList: LiveData<List<SongData>> get() = _baseSongDataList
    private val _baseSongDataList = MutableLiveData<List<SongData>>()

    init {
        loadAllSongs()
    }

    fun loadAllSongs() {
        _baseSongDataList.value = getNewSongsFromDb()
    }

    fun searchSongsByName(): List<SongData> {
        val word = searchWord.value ?: ""

        val songData = baseSongDataList.value ?: listOf()
        return if (word == "") songData
        else songData.filter { it.nameWithDifficultyLabel().contains(word, true) }
    }

    fun resetSearchWord() {
        searchWord.value = ""
    }

    private fun getNewSongsFromDb(): List<SongData> {
        return db.getNewSongs().map { it.convertToSongData() }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class EstimateByNameViewModelFactory(
    private val db: IDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstimateByNameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return EstimateByNameViewModel(db) as T//, spreadSheetService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}