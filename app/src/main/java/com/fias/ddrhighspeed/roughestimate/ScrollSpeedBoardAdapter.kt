package com.fias.ddrhighspeed.roughestimate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.databinding.ResultRowBinding
import com.fias.ddrhighspeed.shared.model.ResultRow

/**
 * Adapter for the [RecyclerView] in [RoughEstimateFragment]. Displays [ResultRow] data object.
 */
class ScrollSpeedBoardAdapter :
    ListAdapter<ResultRow, ScrollSpeedBoardAdapter.ScrollSpeedBoardViewHolder>(DiffCallback) {

    class ScrollSpeedBoardViewHolder(private var binding: ResultRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: ResultRow) {
            binding.bpmView.text = row.bpmRange
            binding.highSpeedView.text = row.highSpeed
            binding.scrollSpeedView.text = row.scrollSpeedRange
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollSpeedBoardViewHolder {
        return ScrollSpeedBoardViewHolder(
            ResultRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScrollSpeedBoardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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