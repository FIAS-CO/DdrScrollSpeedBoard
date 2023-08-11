package com.fias.ddrhighspeed.search.songsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.databinding.SearchResultBinding
import com.fias.ddrhighspeed.shared.cache.SongIndex

class SearchedSongsAdapter(private val clickListener: ClickSongListener) :
    ListAdapter<SongIndex, SearchedSongsAdapter.SearchedSongsViewHolder>(DiffCallback) {

    class SearchedSongsViewHolder(private var binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SongIndex) {
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
        private val DiffCallback = object : DiffUtil.ItemCallback<SongIndex>() {
            override fun areItemsTheSame(oldItem: SongIndex, newItem: SongIndex): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: SongIndex, newItem: SongIndex): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class ClickSongListener(val clickListener: (song: SongIndex) -> Unit) {
    fun onClick(song: SongIndex) = clickListener(song)
}