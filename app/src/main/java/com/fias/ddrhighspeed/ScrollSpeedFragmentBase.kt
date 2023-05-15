package com.fias.ddrhighspeed

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.fias.ddrhighspeed.data.InputDataStore
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private const val INPUT_PREFERENCES_NAME = "input_preferences"
private val Context.inputDataStore: DataStore<Preferences> by preferencesDataStore(
    name = INPUT_PREFERENCES_NAME
)

abstract class ScrollSpeedFragmentBase : Fragment() {

    val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()

    // 継承先Fragmentのスクロールスピード入力欄を指定
    protected abstract val scrollSpeedTextBox: TextInputEditText

    private val inputDataStore: InputDataStore by lazy { InputDataStore(requireContext().inputDataStore) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSavedScrollSpeed()
    }

    protected fun onScrollSpeedChange(suppressSave: Boolean = false) {
        val scrollSpeed = sharedViewModel.getScrollSpeedValue()

        if ((scrollSpeed == null) || (scrollSpeed < 30) || (2000 < scrollSpeed)) {
            scrollSpeedTextBox.error = "30 ～ 2000までの数値を入力してください。"
        } else {
            scrollSpeedTextBox.error = null
        }

        if (suppressSave) return
        saveScrollSpeed()
    }

    private fun saveScrollSpeed() {
        val inputSpeedStr = sharedViewModel.scrollSpeed.value.toString()

        // Launch a coroutine and write the layout setting in the preference Datastore
        lifecycleScope.launch {
            inputDataStore.saveInputScrollSpeedStore(inputSpeedStr)
        }
    }

    private fun loadSavedScrollSpeed() {
        lifecycleScope.launch {
            val savedScrollSpeed = inputDataStore.getScrollSpeed()
            // TODO 初回400が入る処理をFragmentに移したい。
            savedScrollSpeed?.let {
                sharedViewModel.scrollSpeed.setValue(it)
                onScrollSpeedChange(true)
            }
        }
    }
}
