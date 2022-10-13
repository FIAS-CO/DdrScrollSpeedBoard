package com.example.ddrscrollspeedboard.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class InputDataStore(context: Context) {
    private val _inputScrollSpeedKey = stringPreferencesKey("input_scroll_speed")
    private val _displayedTopRowIndexKey = intPreferencesKey("displayed_top_row_index")

    suspend fun saveInputScrollSpeedStore(scrollSpeed: String, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[_inputScrollSpeedKey] = scrollSpeed
        }
    }

    // TODO マジックナンバーを fragment あたりに移動したい
    val scrollSpeedFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[_inputScrollSpeedKey] ?: "400"
        }

    suspend fun saveDisplayTopRowIndexStore(topRowIndex: Int, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[_displayedTopRowIndexKey] = topRowIndex
        }
    }

    val topRowIndexFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[_displayedTopRowIndexKey] ?: 4
        }
}