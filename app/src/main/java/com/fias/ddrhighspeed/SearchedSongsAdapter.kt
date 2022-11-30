package com.fias.ddrhighspeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fias.ddrhighspeed.databinding.SearchResultBinding
import com.fias.ddrhighspeed.model.Song

class SearchedSongsAdapter :
    ListAdapter<Song, SearchedSongsAdapter.SearchedSongsViewHolder>(DiffCallback) {

    class SearchedSongsViewHolder(private var binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.searchResultName.text = song.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedSongsViewHolder {
        return SearchedSongsViewHolder(
            SearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchedSongsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }
        }
    }

}