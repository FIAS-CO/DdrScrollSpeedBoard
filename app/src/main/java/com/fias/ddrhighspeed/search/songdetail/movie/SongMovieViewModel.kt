package com.fias.ddrhighspeed.search.songdetail.movie

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fias.ddrhighspeed.shared.ColorRgba
import com.fias.ddrhighspeed.shared.MovieUtil
import com.fias.ddrhighspeed.shared.cache.IDatabase

class SongMovieViewModel(private val db: IDatabase, private val songId: Long) : ViewModel() {

    val movieList: List<MovieModel> by lazy { loadMovies(songId) }
    private val movieUtil = MovieUtil()

    private fun loadMovies(songId: Long): List<MovieModel> {
        return movieUtil.createMovieData(db.getMovies(songId)).map {
            MovieModel(it.first, colorRgbaToColor(it.second), it.third)
        }
    }

    private fun colorRgbaToColor(rgba: ColorRgba): Color {
        return Color(rgba.red, rgba.green, rgba.blue, rgba.alpha)
    }
}

data class MovieModel(val label: String, val color: Color, val url: String)

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class SongMovieViewModelFactory(private val db: IDatabase, private val songId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongMovieViewModel(db, songId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}