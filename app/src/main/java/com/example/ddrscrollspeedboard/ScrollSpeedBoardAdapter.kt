package com.example.ddrscrollspeedboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ddrscrollspeedboard.databinding.ResultRowBinding
import com.example.ddrscrollspeedboard.model.ResultRow
import com.example.ddrscrollspeedboard.model.ResultRowSetFactory

/**
 * Adapter for the [RecyclerView] in [ScrollSpeedBoardFragment]. Displays [ResultRow] data object.
 */
class ScrollSpeedBoardAdapter() :
    ListAdapter<ResultRow, ScrollSpeedBoardAdapter.ScrollSpeedBoardViewHolder>(DiffCallback) {

    class ScrollSpeedBoardViewHolder(private var binding: ResultRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: ResultRow) {
            binding.bpmView.text = row.bpmRange
            binding.highSpeedView.text = row.highSpeed
            binding.scrollSpeedView.text = row.scrollSpeedRange
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollSpeedBoardViewHolder {
//        val adapterLayout = LayoutInflater.from(parent.context)
//            .inflate(R.layout.result_row, parent, false)
//
//        return ScrollSpeedBoardViewHolder(adapterLayout)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollSpeedBoardViewHolder {
        @Suppress("UnnecessaryVariable")
        val viewHolder = ScrollSpeedBoardViewHolder(
            ResultRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        // クリック時の操作を追加するときはサンプルのBusStopを参考に
//        viewHolder.itemView.setOnClickListener {
//            val position = viewHolder.adapterPosition
//            onItemClicked(getItem(position))
//        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ScrollSpeedBoardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getCurrentList(): MutableList<ResultRow> {
        return super.getCurrentList()
    }

    fun submitScrollSpeedBoard(inputSpeed: String) {
        val scrollSpeed = inputSpeed.toIntOrNull()

        scrollSpeed?.let {
            val resultRowSet = ResultRowSetFactory().create(scrollSpeed)
            submitList(resultRowSet)
        }
    }

    // TODO fragment で submitList を呼び出しても動かない
    fun submitScrollSpeedBoard(rowResults: List<ResultRow>) = submitList(rowResults)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ResultRow>() {
            override fun areItemsTheSame(oldItem: ResultRow, newItem: ResultRow): Boolean {
                return oldItem.highSpeed == newItem.highSpeed
            }

            override fun areContentsTheSame(oldItem: ResultRow, newItem: ResultRow): Boolean {
                return oldItem == newItem
            }
        }
    }
}