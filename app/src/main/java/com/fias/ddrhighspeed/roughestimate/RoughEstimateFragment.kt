package com.fias.ddrhighspeed.roughestimate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedFragmentBase
import com.fias.ddrhighspeed.data.ScrollPositionDataStore
import com.fias.ddrhighspeed.databinding.FragmentRoughEstimateBinding
import com.fias.ddrhighspeed.view.HighSpeedListView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

private const val POSITION_PREFERENCES_NAME = "position_preferences"
private val Context.positionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = POSITION_PREFERENCES_NAME
)

class RoughEstimateFragment : ScrollSpeedFragmentBase() {
    private val viewModel: RoughEstimateViewModel by viewModels()
    private val scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter by lazy { ScrollSpeedBoardAdapter() }
    private val positionDataStore: ScrollPositionDataStore by lazy {
        ScrollPositionDataStore(
            requireContext().positionDataStore
        )
    }

    private lateinit var binding: FragmentRoughEstimateBinding

    override val scrollSpeedTextBox: TextInputEditText
        get() = binding.textInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentRoughEstimateBinding?>(
            inflater,
            R.layout.fragment_rough_estimate,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.boardViewModel = sharedViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                val rows = viewModel.createResultRows(sharedViewModel.getScrollSpeedValue())
                scrollSpeedBoardAdapter.submitList(rows)

                onScrollSpeedChange()
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)

        binding.incrementUp.setSpinButtonListener(sharedViewModel.countUp)
        binding.incrementDown.setSpinButtonListener(sharedViewModel.countDown)

        val recyclerView = binding.recyclerView.apply {
            adapter = scrollSpeedBoardAdapter
            addSaveScrollPositionListener { saveScrollPosition(this) }
        }

        loadSavedListPosition(recyclerView)
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
