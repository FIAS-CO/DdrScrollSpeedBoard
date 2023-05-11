package com.fias.ddrhighspeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class EstimateByNameViewModel(private val db: Database) : ViewModel() {
    private val rsFactory = ResultRowSetFactory()

    // TODO 別のViewModelに。使う範囲が違うので
    val searchWord = MutableLiveData<String>()

    fun createRows(scrollSpeedValue: Int?, song: Song?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        return createRows(value, song)
    }

    private fun createRows(scrollSpeed: Int, song: Song?): List<ResultRowForDetail> {
        val list = mutableListOf<ResultRowForDetail>()
        song?.apply {
            max_bpm?.let {
                list.add(rsFactory.createForDetail(scrollSpeed, "最大", it))
            }
            min_bpm?.let {
                list.add(rsFactory.createForDetail(scrollSpeed, "最小", it))
            }
            base_bpm?.let {
                list.add(rsFactory.createForDetail(scrollSpeed, "基本①", it))
            }
            sub_bpm?.let {
                list.add(rsFactory.createForDetail(scrollSpeed, "基本②", it))
            }
            list.sortDescending() // BPM でソート
        }
        return list
    }

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