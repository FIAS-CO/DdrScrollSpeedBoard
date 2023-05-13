package com.fias.ddrhighspeed

import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class SongDetailViewModel: ViewModel() {
    // TODO かならずsongに値が入っている状態にする
    lateinit var song: Song
    private val rsFactory = ResultRowSetFactory()

    fun createRows(scrollSpeedValue: Int?, song: Song?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        val list = mutableListOf<ResultRowForDetail>()
        song?.apply {
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