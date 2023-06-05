package com.fias.ddrhighspeed.shared

import com.fias.ddrhighspeed.shared.cache.Movie

class MovieUtil {
    private val sortOrder = listOf("BESP", "BSP", "DSP", "ESP", "CSP", "BDP", "DDP", "EDP", "CDP")

    private val cyan = ColorRgba(0f, 0.79f, 0.79f, 1.0f)
    private val orange = ColorRgba(0.98f, 0.63f, 0f, 1.0f)
    private val red = ColorRgba(1f, 0f, 0f, 0.75f)
    private val purple = ColorRgba(0.5f, 0f, 0.5f, 1.0f)
    private val green = ColorRgba(0f, 0.5f, 0f, 1.0f)
    private val gray = ColorRgba(0.5f, 0.5f, 0.5f, 1.0f)

    fun createMovieData(movieList: List<Movie>): List<Triple<String, ColorRgba, String>> {
        return sortByDifficulty(movieList)
            .map {
                val (label, colorRgba) = getLabel(it.difficulty)
                Triple(label, colorRgba, it.movie_id)
            }
    }

    private fun getLabel(difficulty: String): Pair<String, ColorRgba> {
        return when (difficulty) {
            "BESP" -> Pair("Single Beginner", cyan)
            "BSP" -> Pair("Single Basic", orange)
            "DSP" -> Pair("Single Difficult", red)
            "ESP" -> Pair("Single Expert", green)
            "CSP" -> Pair("Single Challenge", purple)
            "BDP" -> Pair("Double Basic", orange)
            "DDP" -> Pair("Double Difficult", red)
            "EDP" -> Pair("Double Expert", green)
            "CDP" -> Pair("Double Challenge", purple)
            else -> Pair("Undefined Difficulty", gray)
        }
    }

    private fun sortByDifficulty(movieList: List<Movie>): List<Movie> {
        return movieList.sortedBy { sortOrder.indexOf(it.difficulty) }
    }
}

data class ColorRgba(val red: Float, val green: Float, val blue: Float, val alpha: Float)