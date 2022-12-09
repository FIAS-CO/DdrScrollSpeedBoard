package com.fias.ddrhighspeed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.database.Song
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.model.ResultRowSetFactory
import com.fias.ddrhighspeed.viewmodels.SongViewModel
import com.fias.ddrhighspeed.viewmodels.SongViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstimateByNameFragment : Fragment() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels {
        SongViewModelFactory(
            (activity?.application as SongApplication).database.scheduleDao()
        )
    }

    private lateinit var scrollSpeedBoardAdapter: ScrollSpeedBoardAdapter
    private lateinit var searchedSongsAdapter: SearchedSongsAdapter

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

        val resultRowSetFactory = ResultRowSetFactory()

        switchDetailViews(View.GONE)

        val clickListener = ClickSongListener { song: Song ->
            goToDetail(song.name)

            val scrollSpeedValue = sharedViewModel.getScrollSpeedValue() ?: 0
            // TODO 0.0 を置き換え
            val list = resultRowSetFactory.createForSongDetail(
                scrollSpeedValue,
                song.minBpm ?: 0.0,
                song.baseBpm ?: 0.0,
                song.maxBpm ?: 0.0,
            )
            scrollSpeedBoardAdapter.submitList(list)
        }
        searchedSongsAdapter = SearchedSongsAdapter(clickListener)

        binding.searchedSongs.adapter = searchedSongsAdapter

        binding.backToSearchImage.setOnClickListener {
            backToSearch()
        }

        val searchWordObserver = Observer<String> {
            CoroutineScope(Dispatchers.IO).launch {
                val searchList: List<Song> =
                    songViewModel.songForSearch(sharedViewModel.searchWord.value.toString())
                handler.post {
                    searchedSongsAdapter.submitList(searchList)
                }
            }
        }
        sharedViewModel.searchWord.observe(viewLifecycleOwner, searchWordObserver)

        scrollSpeedBoardAdapter = ScrollSpeedBoardAdapter()
        binding.songDetailList.apply {
            adapter = scrollSpeedBoardAdapter
        }

        // TODO ScrollSpeed 変更時に ScrollSpeedBoard が更新されるようコード追加
    }

    private fun backToSearch() {
        switchDetailViews(View.GONE)
        switchSearchViews(View.VISIBLE)
    }

    private fun goToDetail(songName: String) {
        switchSearchViews(View.GONE)
        displayDetailViews(songName)
    }

    private fun displayDetailViews(songName: String) {
        switchDetailViews(View.VISIBLE)
        binding.songName.text = songName
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
    }
}