package com.fias.ddrhighspeed.roughestimate

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fias.ddrhighspeed.shared.model.ResultRow
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory

class RoughEstimateViewModel: ViewModel() {

    fun createResultRows(scrollSpeed : Int?): List<ResultRow> {
        val value = scrollSpeed  ?: 0
        Log.d(javaClass.name, "board not updated.")

        return ResultRowSetFactory().create(value)
    }
}