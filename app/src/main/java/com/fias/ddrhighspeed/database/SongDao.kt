package com.fias.ddrhighspeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongDao {

    @Query("SELECT * FROM Songs WHERE name LIKE '%' || :searchWord || '%' ORDER BY name ASC")
    fun getByNameContainWord(searchWord: String): List<Song>

    @Query("SELECT * FROM Songs ORDER BY id DESC")
    fun getNew(): List<Song>

    @Insert(entity = Song::class)
    fun insert(song: List<Song>)
}