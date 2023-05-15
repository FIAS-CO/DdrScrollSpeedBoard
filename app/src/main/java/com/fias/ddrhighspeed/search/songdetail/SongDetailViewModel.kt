package com.fias.ddrhighspeed.search.songdetail

import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class SongDetailViewModel: ViewModel() {
    lateinit var song: SongData
    private val rsFactory = ResultRowSetFactory()

    fun createRows(scrollSpeedValue: Int?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        val list = mutableListOf<ResultRowForDetail>()
        song.apply {
            max_bpm?.let {
                list.add(rsFactory.createForDetail(value, "最大", it))
            }
            min_bpm?.let {
                list.add(rsFactory.createForDetail(value, "最小", it))
            }
            base_bpm?.let {
                list.add(rsFactory.createForDetail(value, "基本①", it))
            }
            sub_bpm?.let {
                list.add(rsFactory.createForDetail(value, "基本②", it))
            }
            list.sortDescending() // BPM でソート
        }
        return list
    }
}