package com.fias.ddrhighspeed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fias.ddrhighspeed.data.InputDataStore
import com.fias.ddrhighspeed.data.ScrollPositionDataStore
import com.fias.ddrhighspeed.databinding.FragmentRoughEstimateBinding
import com.fias.ddrhighspeed.view.HighSpeedListView
import kotlinx.coroutines.launch

private const val POSITION_PREFERENCES_NAME = "position_preferences"
private val Context.positionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = POSITION_PREFERENCES_NAME
)

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private const val INPUT_PREFERENCES_NAME = "input_preferences"
private val Context.inputDataStore: DataStore<Preferences> by preferencesDataStore(
    name = INPUT_PREFERENCES_NAME
)

class RoughEstimateFragment : Fragment() {
    private var _fragmentBinding: FragmentRoughEstimateBinding? = null
    private val binding get() = _fragmentBinding!!
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()
    private val viewModel: RoughEstimateViewModel by viewModels()

    private lateinit var inputDataStore: InputDataStore

    private val scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter by lazy { ScrollSpeedBoardAdapter() }
    private val positionDataStore: ScrollPositionDataStore by lazy {
        ScrollPositionDataStore(
            requireContext().positionDataStore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentRoughEstimateBinding?>(
            inflater,
            R.layout.fragment_rough_estimate,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.boardViewModel = this.sharedViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incrementUp.setSpinButtonListener(sharedViewModel.countUp)
        binding.incrementDown.setSpinButtonListener(sharedViewModel.countDown)

        val recyclerView = binding.recyclerView.apply {
            adapter = scrollSpeedBoardAdapter
            addSaveScrollPositionListener { saveScrollPosition(this) }
        }
        inputDataStore = InputDataStore(requireContext().inputDataStore)
        loadSavedScrollSpeed()

        loadSavedListPosition(recyclerView)

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                val rows = viewModel.createResultRows(sharedViewModel.getScrollSpeedValue())
                scrollSpeedBoardAdapter.submitList(rows)

                onScrollSpeedChange()
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)
    }


    private fun onScrollSpeedChange(suppressSave: Boolean = false) {
        val scrollSpeed = sharedViewModel.getScrollSpeedValue()

        if ((scrollSpeed == null) || (scrollSpeed < 30) || (2000 < scrollSpeed)) {
            binding.textInputEditText.error = "30 ～ 2000までの数値を入力してください。"
        } else {
            binding.textInputEditText.error = null
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

    private fun loadSavedListPosition(recyclerView: HighSpeedListView) {
        lifecycleScope.launch {
            val savedScrollPosition = positionDataStore.getScrollPosition()
            savedScrollPosition?.let {
                val index = it.first
                val offset = it.second
                recyclerView.getLinearLayoutManager()
                    .scrollToPositionWithOffset(index, offset)
            }
        }
    }

    private fun saveScrollPosition(recyclerView: HighSpeedListView) {
        val linearLayoutManager = recyclerView.getLinearLayoutManager()
        val position = linearLayoutManager.findFirstVisibleItemPosition()

        val startView = recyclerView.getChildAt(0)
        val positionOffset = startView.top - recyclerView.paddingTop

        lifecycleScope.launch {
            positionDataStore.saveScrollPositionStore(position, positionOffset)
        }
    }
}
