package com.fias.ddrhighspeed.search.songdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedFragmentBase
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.databinding.FragmentSongDetailBinding
import com.fias.ddrhighspeed.view.AdViewUtil
import com.fias.ddrhighspeed.view.MarqueeToolbar
import com.google.android.material.textfield.TextInputEditText

class SongDetailFragment(private val songData: SongData) : ScrollSpeedFragmentBase() {
    override val scrollSpeedTextBox: TextInputEditText
        get() = binding.textInputEditText

    private var _fragmentBinding: FragmentSongDetailBinding? = null
    private val binding get() = _fragmentBinding!!
    private val detailBoardAdapter: DetailBoardAdapter by lazy { DetailBoardAdapter() }
    private val viewModel: SongDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentSongDetailBinding?>(
            inflater,
            R.layout.fragment_song_detail,
            container,
            false
        ).also {
            it.fragmentViewModel = this.viewModel
            it.viewModel = this.sharedViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        viewModel.song = songData

        val toolbar = requireActivity().findViewById<MarqueeToolbar>(R.id.toolbar)
        toolbar.title = viewModel.songName
        toolbar.startMarqueeScroll()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incrementUp.setSpinButtonListener(sharedViewModel.countUp)
        binding.incrementDown.setSpinButtonListener(sharedViewModel.countDown)

        binding.songDetailList.apply {
            adapter = detailBoardAdapter
        }
        updateTable()

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                updateTable()
                onScrollSpeedChange()
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)

        AdViewUtil().loadAdView(binding.adView, requireContext())
    }

    private fun updateTable() {
        val list = viewModel.createRows(sharedViewModel.getScrollSpeedValue())
        detailBoardAdapter.submitList(list)
    }
}