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

    override fun getSongsById(id: Long): List<Song> {
        return dbQuery.getSongsById(id).executeAsList()
    }

    override fun reinitializeSongNames(songNames: List<SongName>) {
        dbQuery.transaction {
            dbQuery.deleteSongNames()
            songNames.forEach {
                dbQuery.insertSongName(it)
            }
        }
    }

    override fun reinitializeShockArrowExists(shockArrowExists: List<ShockArrowExists>) {
        dbQuery.transaction {
            dbQuery.deleteShockArrowExists()
            shockArrowExists.forEach {
                dbQuery.insertShockArrowExists(it)
            }
        }
    }

    override fun reinitializeWebMusicIds(webMusicIds: List<WebMusicId>) {
        dbQuery.transaction {
            dbQuery.deleteWebMusicIds()
            webMusicIds.forEach {
                dbQuery.insertWebMusicId(it)
            }
        }
    }

    override fun reinitializeSongProperties(songProperties: List<SongProperty>) {
        dbQuery.transaction {
            dbQuery.deleteSongProperties()
            songProperties.forEach {
                dbQuery.insertSongProperty(it)
            }
        }
    }

    override fun getNewSongIndice(): List<SongIndex> {
        return dbQuery.getNewSongIndice().executeAsList()
    }

    override fun searchSongIndiceByName(searchWord: String): List<SongIndex> {
        return dbQuery.searchNewSongIndice(searchWord).executeAsList()
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
