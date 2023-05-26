package com.fias.ddrhighspeed.shared.cache

class Database(databaseDriverFactory: DatabaseDriverFactory) : IDatabase {
    private var driver = databaseDriverFactory.createDriver()
    private val database = AppDatabase(driver)
    private val dbQuery = database.appDatabaseQueries

    override fun searchSongsByName(searchWord: String): List<Song> {
        return dbQuery.getByNameContainWord(searchWord).executeAsList()
    }

    override fun getNewSongs(): List<Song> {
        return dbQuery.getNew().executeAsList()
    }

    override fun insert(songs: List<Song>) {
        dbQuery.transaction {
            songs.forEach {
                dbQuery.insert(it)
            }
        }
    }

    override fun getMovies(songId: Long): List<Movie> {
        return dbQuery.getMovies(songId).executeAsList()
    }

    override fun migrate() {
        AppDatabase.Schema.migrate(
            driver,
            0,
            AppDatabase.Schema.version
        )
    }
}
