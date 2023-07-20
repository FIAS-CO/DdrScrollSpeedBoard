package com.fias.ddrhighspeed.shared.spreadsheet

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual class SpreadSheetService : ISpreadSheetService {
    actual override fun getHttpClient(): HttpClient = HttpClient(Darwin) { expectSuccess = true }

    override fun getUrlBase(): String =
        "https://docs.google.com/spreadsheets/d/1W1FDy3a0Ty1US3RPohSR_rlraBq2xON9q2yUbOfHZIQ/export?format=tsv&gid="
}