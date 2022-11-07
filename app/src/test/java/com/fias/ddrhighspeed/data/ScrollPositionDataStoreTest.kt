package com.fias.ddrhighspeed.data

import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

private const val POSITION_PREFERENCES_NAME = "position_preferences"

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ScrollPositionDataStoreTest : JunitDataStoreTest(POSITION_PREFERENCES_NAME) {
    private var inputDataStore: ScrollPositionDataStore = ScrollPositionDataStore(dataStore)

    @Test
    fun `ScrollPositionDataStore 入力した値をそのまま帰す`() {
        scope.runTest {
            dataStore.edit { it.clear() }
            inputDataStore.saveScrollPositionStore(10, 15)
            assertThat(inputDataStore.scrollPositionFlow.firstOrNull())
                .isEqualTo(Pair(10, 15))
        }
    }

    @Test
    fun `ScrollPositionDataStore 未入力時に400を返す`() {
        scope.runTest {
            dataStore.edit { it.clear() }
            assertThat(inputDataStore.scrollPositionFlow.firstOrNull())
                .isEqualTo(Pair(4, 0))
        }
    }

    @Test
    fun `ScrollPositionDataStore マイナス値をそのまま還す`() {
        scope.runTest {
            dataStore.edit { it.clear() }
            inputDataStore.saveScrollPositionStore(-1, -111)
            assertThat(inputDataStore.scrollPositionFlow.firstOrNull())
                .isEqualTo(Pair(-1, -111))
        }
    }
}