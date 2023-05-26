package com.fias.ddrhighspeed.shared.cache

interface IDatabase {

    fun searchSongsByName(searchWord: String): List<Song>
    fun getNewSongs(): List<Song>
    fun insert(songs: List<Song>)
    fun getMovies(songId: Long) : List<Movie>
    fun migrate()
}