package com.fias.ddrhighspeed.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val POSITION_PREFERENCES_NAME = "position_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = POSITION_PREFERENCES_NAME
)

/**
 * preferences の中身がひとつ変更されると、すべての値に対して変更通知が走るらしいので
 * 入力値とスクロール位置は別のクラスで管理しています
 */
class ScrollPositionDataStore(private val context: Context) {
    private val _displayedPositionOffsetKey2 = stringPreferencesKey("displayed_position_offset2")

    suspend fun saveScrollPositionStore(topRowIndex: Int, positionOffset: Int) {
        val position = "$topRowIndex,$positionOffset"
        context.dataStore.edit { preferences ->
            preferences[_displayedPositionOffsetKey2] = position
        }
    }

    val scrollPositionFlow: Flow<Pair<Int, Int>> = context.dataStore.data
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            val i = preferences[_displayedPositionOffsetKey2]
            val split = i?.split(",")
            if (split == null) Pair(4, 0)
            else Pair(split[0].toInt(), split[1].toInt())
        }
}