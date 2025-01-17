package com.fias.ddrhighspeed

import com.fias.ddrhighspeed.shared.cache.Course
import com.fias.ddrhighspeed.shared.cache.IDatabase
import com.fias.ddrhighspeed.shared.cache.Movie
import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId

class TestDatabase : IDatabase {
    override fun getNewSongs(): List<Song> {
        return listOf()
    }

    override fun reinitializeSongNames(songNames: List<SongName>) {
    }

    override fun reinitializeShockArrowExists(shockArrowExists: List<ShockArrowExists>) {
    }

    override fun reinitializeWebMusicIds(webMusicIds: List<WebMusicId>) {
    }

    override fun reinitializeSongProperties(songProperties: List<SongProperty>) {
    }

    override fun reinitializeMovies(movies: List<Movie>) {
    }

    override fun reinitializeCourses(courses: List<Course>) {
    }

    override fun getMovies(songId: Long): List<Movie> {
        return emptyList()
    }

    override fun getSongsById(songId: Long): List<Song> {
        TODO("Not yet implemented")
    }

    override fun getNewCourses(): List<Course> {
        TODO("Not yet implemented")
    }

    override fun getSongNameById(songId: Long): SongName {
        TODO("Not yet implemented")
    }

    override fun getSongPropertyById(songId: Long): SongProperty {
        TODO("Not yet implemented")
    }

    override fun migrate() {
    }
}