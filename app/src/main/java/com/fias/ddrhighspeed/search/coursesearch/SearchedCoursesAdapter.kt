package com.fias.ddrhighspeed.search.coursesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.databinding.SearchResultBinding

class SearchedCoursesAdapter(private val clickListener: ClickCourseListener) :
    ListAdapter<CourseData, SearchedCoursesAdapter.SearchedCoursesViewHolder>(DiffCallback) {

    class SearchedCoursesViewHolder(private var binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: CourseData) {
            binding.searchResultName.text = song.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCoursesViewHolder {
        val viewHolder = SearchedCoursesViewHolder(
            SearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                clickListener.onClick(getItem(absoluteAdapterPosition))
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchedCoursesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CourseData>() {
            override fun areItemsTheSame(oldItem: CourseData, newItem: CourseData): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CourseData, newItem: CourseData): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ClickCourseListener(val clickListener: (course: CourseData) -> Unit) {
    fun onClick(course: CourseData) = clickListener(course)
}