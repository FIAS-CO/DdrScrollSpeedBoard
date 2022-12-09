package com.fias.ddrhighspeed.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SongDao {

    @Query("SELECT * FROM Songs WHERE name LIKE '%' || :searchWord || '%'")
    fun getByNameContainWord(searchWord: String): List<Song>
}