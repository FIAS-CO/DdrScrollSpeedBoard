package com.fias.ddrhighspeed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.shared.cache.Database
import com.fias.ddrhighspeed.shared.cache.DatabaseDriverFactory
import com.fias.ddrhighspeed.shared.cache.Song
import com.fias.ddrhighspeed.shared.model.ResultRowForDetail
import com.fias.ddrhighspeed.shared.model.ResultRowSetFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstimateByNameFragment : Fragment() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()
    private val resultRowSetFactory = ResultRowSetFactory()
    private var selectedSong: Song? = null

    private lateinit var detailBoardAdapter: DetailBoardAdapter
    private lateinit var searchedSongsAdapter: SearchedSongsAdapter

    private lateinit var db: Database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentEstimateByNameBinding?>(
            inflater,
            R.layout.fragment_estimate_by_name,
            container,
            false
        ).also {
            it.boardViewModel = this.sharedViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Database(DatabaseDriverFactory(requireContext()))
        db.migrate()

        detailBoardAdapter = DetailBoardAdapter()
        binding.songDetailList.apply {
            adapter = detailBoardAdapter
        }
        switchDetailViews(View.GONE)

        val clickListener = ClickSongListener { song: Song ->
            goToDetail(song)

            val scrollSpeedValue = sharedViewModel.getScrollSpeedValue() ?: 0
            val list = createRows(scrollSpeedValue, selectedSong)
            detailBoardAdapter.submitList(list)
        }
        searchedSongsAdapter = SearchedSongsAdapter(clickListener)

        binding.searchedSongs.adapter = searchedSongsAdapter
        CoroutineScope(Dispatchers.IO).launch {
            val searchList: List<Song> = db.getNewSongs()
            handler.post {
                searchedSongsAdapter.submitList(searchList)
            }
        }

        binding.backToSearchImage.setOnClickListener {
            backToSearch()
        }

        val searchWordObserver = Observer<String> {
            CoroutineScope(Dispatchers.IO).launch {
                val searchWord = sharedViewModel.searchWord.value.toString()
                val searchList: List<Song> =
                    if (searchWord == "") db.getNewSongs()
                    else db.searchSongsByName(searchWord)
                handler.post {
                    searchedSongsAdapter.submitList(searchList)
                }
            }
        }
        sharedViewModel.searchWord.observe(viewLifecycleOwner, searchWordObserver)

        val scrollSpeedObserver = Observer<String> {
            val scrollSpeedValue = sharedViewModel.getScrollSpeedValue() ?: 0

            // スピンボタン長押し時に処理が連続実行されないように
            handler.postDelayed({
                if (scrollSpeedValue == sharedViewModel.getScrollSpeedValue()) {
                    Log.d(
                        javaClass.name,
                        "$scrollSpeedValue, ${sharedViewModel.getScrollSpeedValue()}"
                    )

                    val list = createRows(scrollSpeedValue, selectedSong)
                    detailBoardAdapter.submitList(list)
                } else {
                    Log.d(javaClass.name, "board not updated.")
                }
            }, 200)
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)
    }

    private fun createRows(scrollSpeedValue: Int, song: Song?): MutableList<ResultRowForDetail> {
        val list = mutableListOf<ResultRowForDetail>()
        song?.apply {
            max_bpm?.let {
                list.add(resultRowSetFactory.createForDetail(scrollSpeedValue, "最大", it))
            }
            min_bpm?.let {
                list.add(resultRowSetFactory.createForDetail(scrollSpeedValue, "最小", it))
            }
            base_bpm?.let {
                list.add(resultRowSetFactory.createForDetail(scrollSpeedValue, "基本①", it))
            }
            sub_bpm?.let {
                list.add(resultRowSetFactory.createForDetail(scrollSpeedValue, "基本②", it))
            }
            list.sortDescending() // BPM でソート
        }
        return list
    }

    private fun backToSearch() {
        switchDetailViews(View.GONE)
        switchSearchViews(View.VISIBLE)
        selectedSong = null
    }

    private fun goToDetail(song: Song) {
        switchSearchViews(View.GONE)
        switchDetailViews(View.VISIBLE)
        selectedSong = song
        binding.songName.text = song.name
    }

    private fun switchSearchViews(viewStatus: Int) {
        binding.searchLabel.visibility = viewStatus
        binding.searchWordInput.visibility = viewStatus
        binding.searchedSongs.visibility = viewStatus
    }

    private fun switchDetailViews(viewStatus: Int) {
        binding.backToSearchImage.visibility = viewStatus
        binding.songName.visibility = viewStatus
        binding.songDetailTableHeader.visibility = viewStatus
        binding.songDetailList.visibility = viewStatus
        binding.asterisk.visibility = viewStatus
        binding.explainBasicText.visibility = viewStatus
    }
}