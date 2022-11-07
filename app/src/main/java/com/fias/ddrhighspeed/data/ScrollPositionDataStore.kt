package com.fias.ddrhighspeed.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * preferences の中身がひとつ変更されると、すべての値に対して変更通知が走るらしいので
 * 入力値とスクロール位置は別のクラスで管理しています
 */
class ScrollPositionDataStore(private val dataStore: DataStore<Preferences>) {
    private val _displayedPositionOffsetKey = stringPreferencesKey("displayed_position_offset")

    suspend fun saveScrollPositionStore(topRowIndex: Int, positionOffset: Int) {
        val position = "$topRowIndex,$positionOffset"
        dataStore.edit { preferences ->
            preferences[_displayedPositionOffsetKey] = position
        }
    }

    val scrollPositionFlow: Flow<Pair<Int, Int>> = dataStore.data
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            val i = preferences[_displayedPositionOffsetKey]
            val split = i?.split(",")
            if (split == null) Pair(4, 0)
            else Pair(split[0].toInt(), split[1].toInt())
        }
}