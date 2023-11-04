package com.fias.ddrhighspeed.search.coursedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.convertToSongData
import com.fias.ddrhighspeed.shared.model.CourseData
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory
import com.fias.ddrhighspeed.shared.model.SongData

class CourseDetailViewModel(
    private val db: IDatabase
) : ViewModel() {
    var course: CourseData = CourseData()
        set(value) {
            field = value
            field.apply {
                firstSong = getSongData(firstSongId, firstSongPropertyId)
                secondSong = getSongData(secondSongId, secondSongPropertyId)
                thirdSong = getSongData(thirdSongId, thirdSongPropertyId)
                fourthSong = getSongData(fourthSongId, fourthSongPropertyId)
            }
        }

    private val rsFactory = ResultRowSetFactory()

    lateinit var firstSong: SongData
    lateinit var secondSong: SongData
    lateinit var thirdSong: SongData
    lateinit var fourthSong: SongData

    fun calculate(scrollSpeedValue: Int?, songIndex: Int): Double {
        val value = scrollSpeedValue ?: 0

        val song = when (songIndex) {
            1 -> firstSong
            2 -> secondSong
            3 -> thirdSong
            4 -> fourthSong
            else -> throw IndexOutOfBoundsException()
        }

        return rsFactory.calculateHighSpeed(song.baseBpm, value)
    }

    private fun getSongData(songId: Long, propertyId: Long): SongData {
        return if (propertyId == -1L) {
            db.getSongsById(songId).first().convertToSongData()
        } else {
            val songNameData = db.getSongNameById(songId)
            val propertyData = db.getSongPropertyById(propertyId)

            songNameData.convertToSongData(propertyData)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class CourseDetailViewModelFactory(
    private val db: IDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CourseDetailViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}