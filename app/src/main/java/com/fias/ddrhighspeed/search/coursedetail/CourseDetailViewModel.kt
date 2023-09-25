package com.fias.ddrhighspeed.search.coursedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.search.convertToSongData
import com.fias.ddrhighspeed.search.coursesearch.EstimateCourseViewModel
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.model.ResultRowForCourseDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class CourseDetailViewModel(
    private val db: IDatabase
) : ViewModel() {
    lateinit var course: CourseData
    private val rsFactory = ResultRowSetFactory()

    lateinit var firstSongDetails: List<SongData>
    lateinit var secondSongDetails: List<SongData>
    lateinit var thirdSongDetails: List<SongData>
    lateinit var fourthSongDetails: List<SongData>
    val courseName: String
        get() = course.name

    fun createRows(scrollSpeedValue: Int?): List<ResultRowForCourseDetail> {
        val value = scrollSpeedValue ?: 0
        firstSongDetails = db.getSongsById(course.firstSongId).map { it.convertToSongData() }
        val list = mutableListOf<ResultRowForCourseDetail>()
        val songData = firstSongDetails[0]
        songData.apply {
            val hs = rsFactory.calculateHighSpeed(baseBpm, value)
            val firstRow =
                ResultRowForCourseDetail(name, "$minBpm ～ $maxBpm", hs, "")
//            maxBpm?.let {
//                list.add(rsFactory.createForDetail(value, "最大", it))
//            }
//            minBpm?.let {
//                list.add(rsFactory.createForDetail(value, "最小", it))
//            }
//            baseBpm.let {
//                list.add(rsFactory.createForDetail(value, "基本①", it))
//            }
//            subBpm?.let {
//                list.add(rsFactory.createForDetail(value, "基本②", it))
//            }
            list.add(firstRow)
        }
        return list
    }
}

// TODO ファクトリーつくる

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