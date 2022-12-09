package com.fias.ddrhighspeed.database

import android.app.Application

class SongApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}