package com.fias.ddrhighspeed.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HighSpeedListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    init {
        layoutManager = LinearLayoutManager(context)
        val deco = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        addItemDecoration(deco)
    }

    fun getLinearLayoutManager(): LinearLayoutManager {
        return super.getLayoutManager() as LinearLayoutManager
    }

    fun addSaveScrollPositionListener(func: () -> Unit) {
        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                func()
            }
        })
    }
}