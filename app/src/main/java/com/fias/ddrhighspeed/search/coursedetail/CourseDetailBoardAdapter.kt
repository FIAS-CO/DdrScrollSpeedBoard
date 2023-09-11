package com.fias.ddrhighspeed.search.coursedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.databinding.ResultRowForDetailBinding
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail

/**
 * Adapter for the [RecyclerView] in [SongDetailFragment]. Displays [ResultRowForDetail] data object.
 */
class CourseDetailBoardAdapter :
    ListAdapter<ResultRowForDetail, CourseDetailBoardAdapter.DetailBoardViewHolder>(DiffCallback) {

    class DetailBoardViewHolder(private var binding: ResultRowForDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: ResultRowForDetail) {
            binding.category.text = row.category
            binding.bpmView.text = row.bpm
            binding.highSpeedView.text = row.highSpeed
            binding.scrollSpeedView.text = row.scrollSpeed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailBoardViewHolder {
        return DetailBoardViewHolder(
            ResultRowForDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailBoardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ResultRowForDetail>() {
            override fun areItemsTheSame(
                oldItem: ResultRowForDetail,
                newItem: ResultRowForDetail
            ): Boolean {
                return oldItem.highSpeed == newItem.highSpeed
            }

            override fun areContentsTheSame(
                oldItem: ResultRowForDetail,
                newItem: ResultRowForDetail
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}