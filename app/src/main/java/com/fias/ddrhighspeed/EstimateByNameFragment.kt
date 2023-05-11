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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.shared.cache.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstimateByNameFragment : Fragment() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()
    private var selectedSong: Song? = null

    private val viewModel: EstimateByNameViewModel by viewModels {
        EstimateByNameViewModelFactory(
            (activity?.application as SongApplication).db
        )
    }

    private val detailBoardAdapter: DetailBoardAdapter by lazy { DetailBoardAdapter() }
    private val searchedSongsAdapter: SearchedSongsAdapter by lazy {
        val clickListener = ClickSongListener { song: Song ->
            goToDetail(song)

            val list = viewModel.createRows(sharedViewModel.getScrollSpeedValue(), selectedSong)
            detailBoardAdapter.submitList(list)
        }
        SearchedSongsAdapter(clickListener)
    }

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
            it.fragmentViewModel = this.viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.songDetailList.apply {
            adapter = detailBoardAdapter
        }
        switchDetailViews(View.GONE)

        binding.searchedSongs.adapter = searchedSongsAdapter
        CoroutineScope(Dispatchers.IO).launch {
            handler.post {
                searchedSongsAdapter.submitList(viewModel.getNewSongs())
            }
        }

        binding.backToSearchImage.setOnClickListener {
            backToSearch()
        }

        val searchWordObserver = Observer<String> {
            CoroutineScope(Dispatchers.IO).launch {
                val searchWord = viewModel.searchWord.value.toString()
                handler.post {
                    searchedSongsAdapter.submitList(viewModel.searchSongsByName(searchWord))
                }
            }
        }
        viewModel.searchWord.observe(viewLifecycleOwner, searchWordObserver)

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                val scrollSpeedValue = sharedViewModel.getScrollSpeedValue()
                val list = viewModel.createRows(scrollSpeedValue, selectedSong)
                detailBoardAdapter.submitList(list)
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)
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