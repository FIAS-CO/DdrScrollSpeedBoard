package com.fias.ddrhighspeed.shared.cache

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun searchSongsByName(searchWord:String): List<Song> {
        return dbQuery.getByNameContainWord(searchWord).executeAsList()
    }

    fun getNewSongs(): List<Song> {
        return dbQuery.getNew().executeAsList()
    }

    fun insert(songs: List<Song>) {
        dbQuery.transaction {
            songs.forEach {
                dbQuery.insert(it)
            }
        }
    }
}