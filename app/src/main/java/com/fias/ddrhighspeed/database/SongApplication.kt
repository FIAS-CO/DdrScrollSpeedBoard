package com.fias.ddrhighspeed.database

import android.app.Application
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.DatabaseDriverFactory

class SongApplication : Application() {
    val db: Database by lazy { Database(DatabaseDriverFactory(this)) }
}