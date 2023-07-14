package com.fias.ddrhighspeed.shared.spreadsheet

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class TestSpreadSheetService(private val urlBase: String): ISpreadSheetService {
    override fun getHttpClient(): HttpClient = HttpClient(CIO) { expectSuccess = true }

    override fun getUrlBase(): String = urlBase
}