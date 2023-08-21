package com.fias.ddrhighspeed.shared.spreadsheet

import com.fias.ddrhighspeed.shared.cache.Movie
import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface ISpreadSheetService {
    // 以下2つだけ実装クラスで継承する
    fun getHttpClient(): HttpClient
    fun getUrlBase(): String

    // 以下、プロダクトコードでは継承不要。テスト用SpreadSheetのシートIDが変わる場合は要検討
    // IDだけ別途定義する？
    suspend fun fetchFileVersion(): String = fetchData(getUrlBase() + "334969595")
    suspend fun fetchSongNames(): String = fetchData(getUrlBase() + "0")
    suspend fun fetchShockArrowExists(): String = fetchData(getUrlBase() + "1975740187")
    suspend fun fetchWebMusicIds(): String = fetchData(getUrlBase() + "1376903169")
    suspend fun fetchSongProperties(): String = fetchData(getUrlBase() + "150576510")

    // 製品版：1323920042
    suspend fun fetchMovies(): String = fetchData(getUrlBase() + "204755798")

    suspend fun getNewDataVersion(): Int {
        var sourceVersion = 0

        val result = runCatching {
            val version = fetchFileVersion()
            sourceVersion = version.toInt()
        }

        if (result.isFailure) sourceVersion = -1

        return sourceVersion
    }

    suspend fun createAllData(): SSDataResult {
        val versionDeferred = safeAsync { getNewDataVersion() }
        val songNameDeferred = safeAsync { createSongNames() }
        val musicPropertyDeferred = safeAsync { createMusicProperties() }
        val shockArrowDeferred = safeAsync { createShockArrowExists() }
        val webMusicIdDeferred = safeAsync { createWebMusicIds() }
        val movieDeferred = safeAsync { crateMovies() }

        val versionResult = versionDeferred.await()
        val songNamesResult = songNameDeferred.await()
        val musicPropertiesResult = musicPropertyDeferred.await()
        val shockArrowExistsResult = shockArrowDeferred.await()
        val webMusicIdsResult = webMusicIdDeferred.await()
        val moviesResult = movieDeferred.await()

        // Check for errors and collect them.
        val exceptions = listOf(
            versionResult,
            songNamesResult,
            musicPropertiesResult,
            shockArrowExistsResult,
            webMusicIdsResult,
            moviesResult
        ).mapNotNull { it.exceptionOrNull() }

        // If there were any errors, return FailureResult with the list of exceptions.
        if (exceptions.isNotEmpty()) {
            return FailureResult(exceptions)
        }

        val version = versionResult.getOrNull() ?: 0
        val songNames = songNamesResult.getOrNull() ?: emptyList()
        val musicProperties = musicPropertiesResult.getOrNull() ?: emptyList()
        val shockArrowExists = shockArrowExistsResult.getOrNull() ?: emptyList()
        val webMusicIds = webMusicIdsResult.getOrNull() ?: emptyList()
        val movies = moviesResult.getOrNull() ?: emptyList()

        return SuccessResult(
            version,
            songNames,
            musicProperties,
            shockArrowExists,
            webMusicIds,
            movies
        )
    }

    suspend fun createSongNames(): List<SongName> {
        return fetchSongNames().split("\r\n").map {
            val list = it.split("\t")
            SongName(
                list[0].toLong(),
                list[1],
                list[2],
                list[3],
                list[4].toLong(),
                list[5].toLong(),
                list[6].toLong(),
                list[7].toLong(),
                list[8].toLong(),
                list[9].toLong(),
                list[10].toLong(),
                list[11].toLong(),
                list[12].toLong(),
                list[13].toLong(),
                list[14].toLong()
            )
        }
    }

    suspend fun createShockArrowExists(): List<ShockArrowExists> {
        return fetchShockArrowExists().split("\r\n").map {
            val list = it.split("\t")
            ShockArrowExists(
                list[0].toLong(),
                list[1]
            )
        }
    }

    suspend fun createWebMusicIds(): List<WebMusicId> {
        return fetchWebMusicIds().split("\r\n").map {
            val list = it.split("\t")
            WebMusicId(
                list[0].toLong(),
                list[1],
                list[2],
                list[3].toLong()
            )
        }
    }

    suspend fun createMusicProperties(): List<SongProperty> {
        val lines = fetchSongProperties().split("\r\n")
        return lines.subList(1, lines.size).map {
            val list = it.split("\t")
            SongProperty(
                list[0].toLong(),
                list[1].toLong(),
                list[2],
                list[3].toDouble(),
                list[4].toDouble(),
                list[5].toDouble(),
                list[6].toDouble(),
                list[7]
            )
        }
    }

    suspend fun crateMovies(): List<Movie> {
        val lines = fetchMovies().split("\r\n")
        return lines.subList(1, lines.size).map {
            val list = it.split("\t")
            Movie(
                list[0].toLong(), // id
                list[1].toLong(), // song_id
                list[2], // difficulty
                list[3], // site
                list[4], // movie_id
                list[5], // song_name
            )
        }
    }

    suspend fun fetchData(urlString: String): String {
        val response: HttpResponse = getHttpClient().get(urlString)

        return response.bodyAsText()
    }

    fun closeClient() {
        getHttpClient().close()
    }

    private suspend fun <T> safeAsync(block: suspend () -> T): Deferred<Result<T>> {
        return coroutineScope { async { runCatching { block() } } }
    }
}

sealed class SSDataResult

data class SuccessResult(
    val version: Int,
    val songNames: List<SongName>,
    val musicProperties: List<SongProperty>,
    val shockArrowExists: List<ShockArrowExists>,
    val webMusicIds: List<WebMusicId>,
    val movies: List<Movie>
) : SSDataResult()

data class FailureResult(val exceptions: List<Throwable>) : SSDataResult() {
    override fun toString(): String = exceptions[0].stackTraceToString()
}
