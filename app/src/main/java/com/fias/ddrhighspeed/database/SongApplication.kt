package com.fias.ddrhighspeed.database

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.DatabaseDriverFactory

class SongApplication : Application() {
    val db: Database by lazy { Database(DatabaseDriverFactory(this)) }
    val versionDataStore: DataStore<Preferences> by preferencesDataStore(name = "version_preferences")
}