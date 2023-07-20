package com.fias.ddrhighspeed.shared.spreadsheet

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual class SpreadSheetService : ISpreadSheetService {
    actual override fun getHttpClient(): HttpClient = HttpClient(CIO) { expectSuccess = true }

    override fun getUrlBase(): String =
        "https://docs.google.com/spreadsheets/d/1W1FDy3a0Ty1US3RPohSR_rlraBq2xON9q2yUbOfHZIQ/export?format=tsv&gid="
}