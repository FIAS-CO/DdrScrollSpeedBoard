package com.fias.ddrhighspeed.shared.spreadsheet

import com.fias.ddrhighspeed.shared.cache.ShockArrowExists
import com.fias.ddrhighspeed.shared.cache.SongName
import com.fias.ddrhighspeed.shared.cache.SongProperty
import com.fias.ddrhighspeed.shared.cache.WebMusicId
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

interface ISpreadSheetService {
    // 以下2つだけ実装クラスで継承する
    fun getHttpClient(): HttpClient
    fun getUrlBase(): String

    // 以下、プロダクトコードでは継承不要。テスト用SupreadSheetのシートIDが変わる場合は要検討
    // IDだけ別途定義する？
    suspend fun fetchFileVersion(): String = fetchData(getUrlBase() + "334969595")
    suspend fun fetchSongNames(): String = fetchData(getUrlBase() + "0")
    suspend fun fetchShockArrowExists(): String = fetchData(getUrlBase() + "1975740187")
    suspend fun fetchWebMusicIds(): String = fetchData(getUrlBase() + "1376903169")
    suspend fun fetchSongProperties(): String = fetchData(getUrlBase() + "554759448")

    suspend fun getNewDataVersion(): Int {
        var sourceVersion = 0

        val result = runCatching {
            val version = SpreadSheetService().fetchFileVersion()
            sourceVersion = version.toInt()
        }

        if (result.isFailure) sourceVersion = -1

        return sourceVersion
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
        val response: HttpResponse = getHttpClient().get(urlString)

        return response.bodyAsText()
    }

    fun closeClient() {
        getHttpClient().close()
    }
}
