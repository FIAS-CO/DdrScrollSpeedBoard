package com.fias.ddrhighspeed.search.songdetail

import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class SongDetailViewModel : ViewModel() {
    lateinit var song: SongData
    private val rsFactory = ResultRowSetFactory()

    val songName: String
        get() = song.name

    fun createRows(scrollSpeedValue: Int?): List<ResultRowForDetail> {
        val value = scrollSpeedValue ?: 0

        val list = mutableListOf<ResultRowForDetail>()
        song.apply {
            maxBpm?.let {
                list.add(rsFactory.createForDetail(value, "最大", it))
            }
            minBpm?.let {
                list.add(rsFactory.createForDetail(value, "最小", it))
            }
            baseBpm.let {
                list.add(rsFactory.createForDetail(value, "基本①", it))
            }
            subBpm?.let {
                list.add(rsFactory.createForDetail(value, "基本②", it))
            }
            list.removeIf { it.bpm == "0.0" }
            list.sortDescending() // BPM でソート
        }
        return list
    }
}