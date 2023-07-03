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

    override fun insertSongName(songNames: List<SongName>) {
        dbQuery.transaction {
            songNames.forEach {
                dbQuery.insertSongName(it)
            }
        }
    }

    override fun insertShockArrowExists(shockArrowExists: List<ShockArrowExists>) {
        dbQuery.transaction {
            shockArrowExists.forEach {
                dbQuery.insertShockArrowExists(it)
            }
        }
    }

    override fun insertWebMusicId(webMusicIds: List<WebMusicId>) {
        dbQuery.transaction {
            webMusicIds.forEach {
                dbQuery.insertWebMusicId(it)
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
