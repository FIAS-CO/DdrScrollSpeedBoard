package com.fias.ddrhighspeed.shared.cache

interface IDatabase {

    fun searchSongsByName(searchWord: String): List<Song>
    fun getNewSongs(): List<Song>
    fun insertSongName(songNames: List<SongName>)
    fun insertShockArrowExists(shockArrowExists: List<ShockArrowExists>)
    fun insertWebMusicId(webMusicIds: List<WebMusicId>)
    fun getMovies(songId: Long) : List<Movie>
    fun migrate()
}