package com.fias.ddrhighspeed.shared.cache

interface IDatabase {

    fun searchSongsByName(searchWord: String): List<Song>
    fun searchSongIndiceByName(searchWord: String): List<SongIndex>
    fun getNewSongs(): List<Song>
    fun getSongsById(id: Long): List<Song>
    fun reinitializeSongNames(songNames: List<SongName>)
    fun reinitializeShockArrowExists(shockArrowExists: List<ShockArrowExists>)
    fun reinitializeWebMusicIds(webMusicIds: List<WebMusicId>)
    fun reinitializeSongProperties(songProperties: List<SongProperty>)
    fun getNewSongIndice(): List<SongIndex>
    fun getMovies(songId: Long): List<Movie>
    fun migrate()
}