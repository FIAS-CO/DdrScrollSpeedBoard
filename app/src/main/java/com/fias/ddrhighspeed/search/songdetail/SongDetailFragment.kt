package com.fias.ddrhighspeed.search.songdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedFragmentBase
import com.fias.ddrhighspeed.databinding.FragmentSongDetailBinding
import com.google.android.material.textfield.TextInputEditText

class SongDetailFragment : ScrollSpeedFragmentBase() {
    override val scrollSpeedTextBox: TextInputEditText
        get() = binding.textInputEditText

    private var _fragmentBinding: FragmentSongDetailBinding? = null
    private val binding get() = _fragmentBinding!!
    private val detailBoardAdapter: DetailBoardAdapter by lazy { DetailBoardAdapter() }
    private val viewModel: SongDetailViewModel by activityViewModels()
    private val args: SongDetailFragmentArgs by navArgs()

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
        viewModel.song = args.song

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songName.text = viewModel.song.name

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
    }

    private fun updateTable() {
        val list = viewModel.createRows(sharedViewModel.getScrollSpeedValue())
        detailBoardAdapter.submitList(list)
    }
}