package com.fias.ddrhighspeed.search.coursedetail

import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.search.convertToSongData
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class CourseDetailViewModel(
    private val db: IDatabase
) : ViewModel() {
    var course: CourseData
        get() {
            TODO()
        }
        set(value) {
            firstSongDetails = db.getSongsById(course.firstSongId).map { it.convertToSongData() }
        }
    private val rsFactory = ResultRowSetFactory()

    lateinit var firstSongDetails: List<SongData>
    lateinit var secondSongDetails: List<SongData>
    lateinit var thirdSongDetails: List<SongData>
    lateinit var fourthSongDetails: List<SongData>
    val courseName: String
        get() = course.name

    fun createRows(scrollSpeedValue: Int?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        val list = mutableListOf<ResultRowForDetail>()
        course.apply {
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
            list.removeIf { it.bpm == "0.0" }
            list.sortDescending() // BPM でソート
        }
        return list
    }
}