package com.fias.ddrhighspeed.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.database.Song
import com.fias.ddrhighspeed.database.SongDao

class SongViewModel(private val songDao: SongDao) : ViewModel() {
    fun searchSongsByName(searchWord: String): List<Song> = songDao.getByNameContainWord(searchWord)
    fun getNewSongs(): List<Song> = songDao.getNew()
}

class SongViewModelFactory(
    private val songDao: SongDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(songDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}