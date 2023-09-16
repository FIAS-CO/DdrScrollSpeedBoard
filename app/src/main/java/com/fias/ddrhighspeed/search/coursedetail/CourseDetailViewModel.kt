package com.fias.ddrhighspeed.search.coursedetail

import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class CourseDetailViewModel : ViewModel() {
    lateinit var course: CourseData
    private val rsFactory = ResultRowSetFactory()

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