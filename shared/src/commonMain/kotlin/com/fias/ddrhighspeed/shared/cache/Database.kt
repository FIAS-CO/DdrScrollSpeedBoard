package com.fias.ddrhighspeed.shared.cache

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private var driver = databaseDriverFactory.createDriver()
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    fun searchSongsByName(searchWord: String): List<Song> {
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

    fun getSpMovies(songId: Long) : List<SpMovie> {
        return dbQuery.getMovies(songId).executeAsList()
    }

    fun migrate() {
        AppDatabase.Schema.migrate(
            driver,
            0,
            AppDatabase.Schema.version
        )
    }
}
