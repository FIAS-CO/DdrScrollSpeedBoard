package com.example.ddrscrollspeedboard

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddrscrollspeedboard.data.InputDataStore
import com.example.ddrscrollspeedboard.databinding.FragmentScrollSpeedBoardBinding
import kotlinx.coroutines.launch

class ScrollSpeedBoardFragment : Fragment() {
    private var _fragmentBinding: FragmentScrollSpeedBoardBinding? = null
    private val binding get() = _fragmentBinding!!
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val viewModel: ScrollSpeedBoardViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var settingsDataStore: InputDataStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentScrollSpeedBoardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()
        recyclerView.adapter = scrollSpeedBoardAdapter

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
                showOffKeyboard()
            }
        }

        val scrollSpeedObserver = Observer<String> {
            val scrollSpeed = viewModel.scrollSpeed.value

            // スピンボタン長押し時にテーブルが更新されないように
            handler.postDelayed({
                if (scrollSpeed == viewModel.scrollSpeed.value) {
                    Log.d(this.javaClass.name, "$scrollSpeed, ${viewModel.scrollSpeed.value}")
                    scrollSpeedBoardAdapter.submitList(viewModel.resultRows())
                } else {
                    Log.d(this.javaClass.name, "board not updated.")
                }
            }, 200)
        }
        viewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)

        // TODO ここになくていいかも。
        settingsDataStore = InputDataStore(requireContext())
        settingsDataStore.scrollSpeedFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            viewModel.setScrollSpeed(value)
            scrollSpeedBoardAdapter.submitList(viewModel.resultRows())
        }
        settingsDataStore.topRowIndexFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            // この中は onViewCreated 全体よりあとで実行される
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(value, 0)
        }
    }

    override fun onPause() {
        super.onPause()

        val inputSpeedStr = binding.textInputEditText.text.toString()
        // Launch a coroutine and write the layout setting in the preference Datastore
        lifecycleScope.launch {
            settingsDataStore.saveInputScrollSpeedStore(inputSpeedStr, requireContext())
        }

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()
        lifecycleScope.launch {
            settingsDataStore.saveDisplayTopRowIndexStore(position, requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    private fun showOffKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}