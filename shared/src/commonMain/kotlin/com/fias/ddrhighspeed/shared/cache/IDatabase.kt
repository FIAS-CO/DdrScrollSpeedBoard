package com.fias.ddrhighspeed.shared.cache

interface IDatabase {

    fun getNewSongs(): List<Song>
    fun reinitializeSongNames(songNames: List<SongName>)
    fun reinitializeShockArrowExists(shockArrowExists: List<ShockArrowExists>)
    fun reinitializeWebMusicIds(webMusicIds: List<WebMusicId>)
    fun reinitializeSongProperties(songProperties: List<SongProperty>)
    fun getMovies(songId: Long): List<Movie>
    fun migrate()
}