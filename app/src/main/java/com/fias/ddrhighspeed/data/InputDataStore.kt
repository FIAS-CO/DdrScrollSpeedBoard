package com.fias.ddrhighspeed.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val INPUT_PREFERENCES_NAME = "input_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = INPUT_PREFERENCES_NAME
)

/**
 * preferences の中身がひとつ変更されると、すべての値に対して変更通知が走るらしいので
 * 入力値とスクロール位置は別のクラスで管理しています
 */
class InputDataStore(private val context: Context) {
    private val _inputScrollSpeedKey = stringPreferencesKey("input_scroll_speed")

    suspend fun saveInputScrollSpeedStore(scrollSpeed: String) {
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
}