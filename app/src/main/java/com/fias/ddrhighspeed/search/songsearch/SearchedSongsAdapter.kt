package com.fias.ddrhighspeed.search.songsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.databinding.SearchResultBinding

class SearchedSongsAdapter(private val clickListener: ClickSongListener) :
    ListAdapter<SongData, SearchedSongsAdapter.SearchedSongsViewHolder>(DiffCallback) {

    class SearchedSongsViewHolder(private var binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongData) {
            binding.searchResultName.text = song.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedSongsViewHolder {
        val viewHolder = SearchedSongsViewHolder(
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

    override fun onBindViewHolder(holder: SearchedSongsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SongData>() {
            override fun areItemsTheSame(oldItem: SongData, newItem: SongData): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: SongData, newItem: SongData): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ClickSongListener(val clickListener: (song: SongData) -> Unit) {
    fun onClick(song: SongData) = clickListener(song)
}