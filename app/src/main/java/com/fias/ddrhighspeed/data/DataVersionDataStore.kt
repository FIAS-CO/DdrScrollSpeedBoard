package com.fias.ddrhighspeed.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * preferences の中身がひとつ変更されると、すべての値に対して変更通知が走るらしいので
 * 入力値とスクロール位置は別のクラスで管理しています
 */
class DataVersionDataStore(private val dataStore: DataStore<Preferences>) : IDataVersionDataStore {
    private val _displayedPositionOffsetKey = intPreferencesKey("displayed_position_offset")

    override suspend fun saveDataVersionStore(topRowIndex: Int) {
        dataStore.edit { preferences ->
            preferences[_displayedPositionOffsetKey] = topRowIndex
        }
    }

    override suspend fun getDataVersion(): Int = dataVersionFlow.first()

    private val dataVersionFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[_displayedPositionOffsetKey] ?: 0
        }
}