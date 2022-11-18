package com.fias.ddrhighspeed.data

import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

private const val INPUT_PREFERENCES_NAME = "input_preferences"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class InputDataStoreTest : JunitDataStoreTest(INPUT_PREFERENCES_NAME) {
    private var inputDataStore: InputDataStore = InputDataStore(dataStore)

    @Test
    fun `inputDataStore 入力した値をそのまま帰す`() {
        scope.runTest {
            dataStore.edit { it.clear() }
            inputDataStore.saveInputScrollSpeedStore("500")
            assertThat(inputDataStore.getScrollSpeed()).isEqualTo("500")
        }
    }

    @Test
    fun `inputDataStore 未入力時に400を返す`() {
        scope.runTest {
            dataStore.edit { it.clear() }
            assertThat(inputDataStore.getScrollSpeed()).isEqualTo("400")
        }
    }
}