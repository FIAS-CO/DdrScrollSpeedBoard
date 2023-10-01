package com.fias.ddrhighspeed.shared.cache

interface IDatabase {
    fun getNewSongs(): List<Song>
    fun reinitializeSongNames(songNames: List<SongName>)
    fun reinitializeShockArrowExists(shockArrowExists: List<ShockArrowExists>)
    fun reinitializeWebMusicIds(webMusicIds: List<WebMusicId>)
    fun reinitializeSongProperties(songProperties: List<SongProperty>)
    fun reinitializeMovies(movies: List<Movie>)
    fun reinitializeCourses(courses: List<Course>)
    fun getMovies(songId: Long): List<Movie>
    fun getSongsById(songId: Long): List<Song>
    fun getNewCourses(): List<Course>
    fun migrate()
}