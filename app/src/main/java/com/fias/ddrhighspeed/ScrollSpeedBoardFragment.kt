package com.fias.ddrhighspeed

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.fias.ddrhighspeed.data.InputDataStore
import com.fias.ddrhighspeed.data.ScrollPositionDataStore
import com.fias.ddrhighspeed.databinding.FragmentScrollSpeedBoardBinding
import com.fias.ddrhighspeed.view.AdViewUtil
import com.fias.ddrhighspeed.view.HighSpeedListView
import kotlinx.coroutines.launch

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private const val INPUT_PREFERENCES_NAME = "input_preferences"
private val Context.inputDataStore: DataStore<Preferences> by preferencesDataStore(
    name = INPUT_PREFERENCES_NAME
)

private const val POSITION_PREFERENCES_NAME = "position_preferences"
private val Context.positionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = POSITION_PREFERENCES_NAME
)

class ScrollSpeedBoardFragment : Fragment() {
    private var _fragmentBinding: FragmentScrollSpeedBoardBinding? = null
    private val binding get() = _fragmentBinding!!
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val viewModel: ScrollSpeedBoardViewModel by viewModels()

    private lateinit var scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter
    private lateinit var inputDataStore: InputDataStore
    private lateinit var positionDataStore: ScrollPositionDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentScrollSpeedBoardBinding?>(
            inflater,
            R.layout.fragment_scroll_speed_board,
            container,
            false
        ).also {
            it.boardViewModel = this.viewModel
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()

        val recyclerView = binding.recyclerView.apply {
            adapter = scrollSpeedBoardAdapter
            addSaveScrollPositionListener { saveScrollPosition(this) }
        }

        inputDataStore = InputDataStore(requireContext().inputDataStore)
        loadSavedScrollSpeed()

        positionDataStore = ScrollPositionDataStore(requireContext().positionDataStore)
        loadSavedListPosition(recyclerView)

        val scrollSpeedLabelView = binding.scrollSpeedLabel
        scrollSpeedLabelView.post {
            // RecyclerView がスクロール可能な場合のぼかし幅を設定
            // できれば行の高さ基準にしたいが、 recyclerView から高さを取得しようとすると0になるので暫定
            recyclerView.setFadingEdgeLength(scrollSpeedLabelView.height * 3)
        }

        binding.incrementUp.setSpinButtonListener(viewModel.countUp)
        binding.incrementDown.setSpinButtonListener(viewModel.countDown)

        val scrollSpeedObserver = Observer<String> {
            val scrollSpeed = viewModel.getScrollSpeedValue()

            // スピンボタン長押し時にテーブルが更新されないように
            handler.postDelayed({
                if (scrollSpeed == viewModel.getScrollSpeedValue()) {
                    Log.d(this.javaClass.name, "$scrollSpeed, ${viewModel.getScrollSpeedValue()}")
                    onScrollSpeedChange()
                } else {
                    Log.d(this.javaClass.name, "board not updated.")
                }
            }, 200)
        }
        viewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)

        AdViewUtil().loadAdView(binding.adView, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    private fun loadSavedScrollSpeed() {
        lifecycleScope.launch {
            val savedScrollSpeed = inputDataStore.getScrollSpeed()
            // TODO 初回400が入る処理をFragmentに移したい。
            savedScrollSpeed?.let {
                viewModel.scrollSpeed.setValue(it)
                // ここで RecyclerView を更新しないとスクロールの初期位置が設定できない
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

    private fun onScrollSpeedChange(suppressSave: Boolean = false) {
        scrollSpeedBoardAdapter.submitList(viewModel.resultRows())
        val scrollSpeed = viewModel.getScrollSpeedValue()

        if ((scrollSpeed == null) || (scrollSpeed < 30) || (2000 < scrollSpeed)) {
            binding.textInputEditText.error = "30 ～ 2000までの数値を入力してください。"
        } else {
            binding.textInputEditText.error = null
        }

        if (suppressSave) return

        saveScrollSpeed()
    }

    private fun saveScrollSpeed() {
        val inputSpeedStr = viewModel.scrollSpeed.value.toString()

        // Launch a coroutine and write the layout setting in the preference Datastore
        lifecycleScope.launch {
            inputDataStore.saveInputScrollSpeedStore(inputSpeedStr)
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