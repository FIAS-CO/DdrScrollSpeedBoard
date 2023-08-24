package com.fias.ddrhighspeed.shared.spreadsheet

import io.ktor.client.*

expect class SpreadSheetService : ISpreadSheetService {
    override fun getHttpClient(): HttpClient
}