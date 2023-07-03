package com.fias.ddrhighspeed.shared

import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class SpreadSheetUtil {
    private val client = HttpClient(CIO) {
        expectSuccess = true
    }

    private val urlBase =
        "https://docs.google.com/spreadsheets/d/1W1FDy3a0Ty1US3RPohSR_rlraBq2xON9q2yUbOfHZIQ/export?format=tsv&&gid="

    suspend fun fetchFileVersion() = fetchData(urlBase + "334969595")
    suspend fun fetchSongNames() = fetchData(urlBase + "0")
    suspend fun fetchShockArrowExists() = fetchData(urlBase + "1975740187")
    suspend fun fetchWebMusicIds() = fetchData(urlBase + "1376903169")
    suspend fun fetchSongProperties() = fetchData(urlBase + "554759448")

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
        return fetchSongProperties().split("\r\n").map {
            val list = it.split("\t")
            SongProperty(
                list[0].toLong(),
                list[1],
                list[2].toDouble(),
                list[3].toDouble(),
                list[4].toDouble(),
                list[5].toDouble()
            )
        }
    }

    suspend fun fetchData(urlString: String): String {
        val response: HttpResponse = client.get(urlString)

        return response.bodyAsText()
    }

    fun closeClient() {
        client.close()
    }
}