package com.fias.ddrhighspeed.data

import android.content.Context
import androidx.annotation.CallSuper
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import org.junit.After
import org.junit.Before
import kotlinx.coroutines.test.*

@ExperimentalCoroutinesApi
abstract class JunitDataStoreTest(dataStoreName :String) {

    private val dispatcher = UnconfinedTestDispatcher()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val scope = TestScope(dispatcher + Job())
    val dataStore = PreferenceDataStoreFactory.create(
        scope = scope.backgroundScope,
        produceFile = { context.preferencesDataStoreFile(dataStoreName) }
    )

    @CallSuper
    @Before
    open fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @CallSuper
    @After
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}