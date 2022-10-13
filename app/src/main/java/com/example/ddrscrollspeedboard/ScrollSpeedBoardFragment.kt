package com.example.ddrscrollspeedboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private val displayedTopIndex = 4

    private lateinit var recyclerView: RecyclerView
    private lateinit var SettingsDataStore: InputDataStore

    // TODO 入力したスクロールスピード
    private val viewModel: ScrollSpeedBoardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentScrollSpeedBoardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textEditView = binding.textInputEditText

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

        binding.button.setOnClickListener {
            viewModel.setScrollSpeed(textEditView.text.toString())
            Log.d("test", viewModel.scrollSpeed)
            Log.d("test", viewModel.resultRows()[0].toString())
//            scrollSpeedBoardAdapter.submitList(viewModel.resultRows)
            scrollSpeedBoardAdapter.submitScrollSpeedBoard(viewModel.resultRows())
            textEditView.clearFocus()
        }

        val incrementUpView = binding.incrementUp
        val spinButtonListener = SpinButtonListener { countUp() }
        with(incrementUpView) {
            // TODO 終了後にリスト更新
            setOnClickListener(spinButtonListener)
            setOnLongClickListener(spinButtonListener)
            setOnTouchListener(spinButtonListener)
        }

        val incrementDownView = binding.incrementDown
        incrementDownView.setOnClickListener {
            // TODO 実装
            countDown()
        }

        textEditView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                showOffKeyboard()
            }
        }

        SettingsDataStore = InputDataStore(requireContext())
        SettingsDataStore.scrollSpeedFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            // この中は onViewCreated 全体よりあとで実行される
            // TODO viewModel と View をつなぐ
            viewModel.setScrollSpeed(value)
            textEditView.setText(viewModel.scrollSpeed)

            scrollSpeedBoardAdapter.submitScrollSpeedBoard(viewModel.resultRows())
        }
        SettingsDataStore.topRowIndexFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            // この中は onViewCreated 全体よりあとで実行される
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(value, 0)
        }
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
//            ScrollSpeedBoardViewModel::class.java
//        )
//        // TODO: Use the ViewModel
//    }

    override fun onPause() {
        super.onPause()

        val inputSpeedStr = binding.textInputEditText.text.toString()
        // Launch a coroutine and write the layout setting in the preference Datastore
        lifecycleScope.launch {
            SettingsDataStore.saveInputScrollSpeedStore(inputSpeedStr, requireContext())
        }

        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = linearLayoutManager.findFirstVisibleItemPosition()
        lifecycleScope.launch {
            SettingsDataStore.saveDisplayTopRowIndexStore(position, requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    // TODO Fragment と共有したい
    fun showOffKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun countUp() {
        val textEditView = binding.textInputEditText

        var input = textEditView.text.toString().toIntOrNull() ?: 1
        input++
        textEditView.setText(input.toString())
    }

    private fun countDown() {
        val textEditView = binding.textInputEditText

        var input = textEditView.text.toString().toIntOrNull() ?: 30
        input = if (input <= 30) 30 else --input
        textEditView.setText(input.toString())
    }
}