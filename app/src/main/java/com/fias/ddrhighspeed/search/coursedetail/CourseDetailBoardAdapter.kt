package com.fias.ddrhighspeed.search.coursedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.databinding.ResultRowForCourseDetailBinding
import com.fias.ddrhighspeed.shared.model.ResultRowForCourseDetail

/**
 * Adapter for the [RecyclerView] in [CourseDetailFragment]. Displays [ResultRowForCourseDetail] data object.
 */
class CourseDetailBoardAdapter :
    ListAdapter<ResultRowForCourseDetail, CourseDetailBoardAdapter.DetailBoardViewHolder>(
        DiffCallback
    ) {

    class DetailBoardViewHolder(private var binding: ResultRowForCourseDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: ResultRowForCourseDetail) {
            binding.category.text = row.songName
            binding.bpmView.text = row.bpm
            binding.highSpeedView.text = row.suggestedHighSpeed.toString()
            binding.scrollSpeedView.text = row.difficulty
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailBoardViewHolder {
        return DetailBoardViewHolder(
            ResultRowForCourseDetailBinding.inflate(
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
        private val DiffCallback = object : DiffUtil.ItemCallback<ResultRowForCourseDetail>() {
            override fun areItemsTheSame(
                oldItem: ResultRowForCourseDetail,
                newItem: ResultRowForCourseDetail
            ): Boolean {
                return oldItem.songName == newItem.songName && oldItem.difficulty == newItem.difficulty
            }

            override fun areContentsTheSame(
                oldItem: ResultRowForCourseDetail,
                newItem: ResultRowForCourseDetail
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}