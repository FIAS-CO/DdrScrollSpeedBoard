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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fias.ddrhighspeed.data.InputDataStore
import com.fias.ddrhighspeed.databinding.FragmentScrollSpeedBoardBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private const val INPUT_PREFERENCES_NAME = "input_preferences"
private val Context.inputDataStore: DataStore<Preferences> by preferencesDataStore(
    name = INPUT_PREFERENCES_NAME
)

//private const val POSITION_PREFERENCES_NAME = "position_preferences"
//private val Context.positionDataStore: DataStore<Preferences> by preferencesDataStore(
//    name = POSITION_PREFERENCES_NAME
//)

class ScrollSpeedBoardFragment : Fragment() {
    private var _fragmentBinding: FragmentScrollSpeedBoardBinding? = null
    private val binding get() = _fragmentBinding!!
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()

    //    private lateinit var scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter
    private lateinit var inputDataStore: InputDataStore
//    private lateinit var positionDataStore: ScrollPositionDataStore

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private val tabTitleArray = arrayOf(
        "Tab1",
        "Tab2",
    )

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
            it.boardViewModel = this.sharedViewModel
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()

        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = demoCollectionAdapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()


//        scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()
//
//        val recyclerView = binding.recyclerView.apply {
//            adapter = scrollSpeedBoardAdapter
//            addSaveScrollPositionListener { saveScrollPosition(this) }
//        }
//
        inputDataStore = InputDataStore(requireContext().inputDataStore)
        loadSavedScrollSpeed()
//
//        positionDataStore = ScrollPositionDataStore(requireContext().positionDataStore)
//        loadSavedListPosition(recyclerView)
//
//        val scrollSpeedLabelView = binding.scrollSpeedLabel
//        scrollSpeedLabelView.post {
//            // RecyclerView がスクロール可能な場合のぼかし幅を設定
//            // できれば行の高さ基準にしたいが、 recyclerView から高さを取得しようとすると0になるので暫定
//            recyclerView.setFadingEdgeLength(scrollSpeedLabelView.height * 3)
//        }
//
        binding.incrementUp.setSpinButtonListener(sharedViewModel.countUp)
        binding.incrementDown.setSpinButtonListener(sharedViewModel.countDown)

        val scrollSpeedObserver = Observer<String> {
            val scrollSpeed = sharedViewModel.getScrollSpeedValue()

            // スピンボタン長押し時にテーブルが更新されないように
            handler.postDelayed({
                if (scrollSpeed == sharedViewModel.getScrollSpeedValue()) {
                    Log.d(
                        this.javaClass.name,
                        "$scrollSpeed, ${sharedViewModel.getScrollSpeedValue()}"
                    )
                    onScrollSpeedChange()
                } else {
                    Log.d(this.javaClass.name, "board not updated.")
                }
            }, 200)
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)
//
//        AdViewUtil().loadAdView(binding.adView, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    private fun onScrollSpeedChange(suppressSave: Boolean = false) {
//        scrollSpeedBoardAdapter.submitList(viewModel.resultRows())
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
                // ここで RecyclerView を更新しないとスクロールの初期位置が設定できない
                onScrollSpeedChange(true)
            }
        }
    }
}

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return when (position) {
            0 -> RoughEstimateFragment()
            1 -> EstimateByNameFragment()
            else -> throw IndexOutOfBoundsException()
        }
//        val fragment = DemoObjectFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
//        return fragment
    }
}