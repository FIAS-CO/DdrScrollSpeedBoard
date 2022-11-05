package com.fias.ddrhighspeed

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.fias.ddrhighspeed.data.InputDataStore
import com.fias.ddrhighspeed.data.ScrollPositionDataStore
import com.fias.ddrhighspeed.databinding.FragmentScrollSpeedBoardBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch

class ScrollSpeedBoardFragment : Fragment() {
    private var _fragmentBinding: FragmentScrollSpeedBoardBinding? = null
    private val binding get() = _fragmentBinding!!
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val viewModel: ScrollSpeedBoardViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputDataStore: InputDataStore
    private lateinit var positionDataStore: ScrollPositionDataStore
    private lateinit var scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter

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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val deco = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(deco)

        scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()
        recyclerView.adapter = scrollSpeedBoardAdapter
        recyclerView.addSaveScrollPositionListener()

        val scrollSpeedLabelView = binding.scrollSpeedLabel
        scrollSpeedLabelView.post {
            // RecyclerView がスクロール可能な場合のぼかし幅を設定
            // できれば行の高さ基準にしたいが、 recyclerView から高さを取得しようとすると0になるので暫定
            recyclerView.setFadingEdgeLength(scrollSpeedLabelView.height * 3)
        }

        val incrementUpView = binding.incrementUp
        incrementUpView.setSpinButtonListener(viewModel.countUp)

        val incrementDownView = binding.incrementDown
        incrementDownView.setSpinButtonListener(viewModel.countDown)

        val textEditView = binding.textInputEditText
        textEditView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideSoftwareKeyboard()
            }
        }

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

        // AdMob 設定
        MobileAds.initialize(requireContext()) {}

        val mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // TODO ここになくていいかも。
        inputDataStore = InputDataStore(requireContext())
        inputDataStore.scrollSpeedFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            // TODO 初回400が入る処理をFragmentに移したい。
            viewModel.scrollSpeed.setValue(value)
            // ここで RecyclerView を更新しないとスクロールの初期位置が設定できない
            onScrollSpeedChange(true)
        }
        positionDataStore = ScrollPositionDataStore(requireContext())
        positionDataStore.scrollPositionFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            // この中は onViewCreated 全体よりあとで実行される
            val index = value.first
            val offset = value.second
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                index,
                offset
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
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

    private fun RecyclerView.addSaveScrollPositionListener() {
        this.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    saveRecyclerViewScrollPosition()
                }
            }
        })
    }

    private fun saveRecyclerViewScrollPosition() {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()

        val startView: View = recyclerView.getChildAt(0)
        val positionOffset = startView.top - recyclerView.paddingTop

        lifecycleScope.launch {
            positionDataStore.saveScrollPositionStore(position, positionOffset)
        }
    }

    private fun hideSoftwareKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}