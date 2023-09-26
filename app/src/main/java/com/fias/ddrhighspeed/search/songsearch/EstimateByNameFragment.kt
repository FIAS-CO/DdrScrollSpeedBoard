package com.fias.ddrhighspeed.search.songsearch

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedBoardFragmentDirections
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.search.DataUpdateViewModel
import com.fias.ddrhighspeed.search.DataUpdateViewModelFactory
import com.fias.ddrhighspeed.shared.spreadsheet.SpreadSheetService
import kotlinx.coroutines.launch

class EstimateByNameFragment : Fragment() {
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val spreadSheetService = SpreadSheetService()
    private val fragmentViewModel: EstimateByNameViewModel by viewModels {
        EstimateByNameViewModelFactory(
            (activity?.application as SongApplication).db
        )
    }

    private val dataUpdateViewModel: DataUpdateViewModel by activityViewModels {
        DataUpdateViewModelFactory(
            (activity?.application as SongApplication).db, spreadSheetService
        )
    }

    private val versionDataStore: DataVersionDataStore by lazy {
        DataVersionDataStore(
            (requireActivity().application as SongApplication).versionDataStore
        )
    }

    private val searchedSongsAdapter: SearchedSongsAdapter by lazy {
        val clickListener = ClickSongListener { song: SongData ->
            val navController = findNavController()
            val action =
                ScrollSpeedBoardFragmentDirections.actionSongSearchToSongDetailPager(song)
            navController.navigate(action)
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
            it.fragmentViewModel = this.fragmentViewModel
            it.dataUpdateViewModel = this.dataUpdateViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchedSongs.adapter = searchedSongsAdapter

        fragmentViewModel.searchWord.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        fragmentViewModel.baseSongDataList.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        dataUpdateViewModel.localDataVersion.observe(viewLifecycleOwner) { version ->
            binding.dataVersion.text = getString(R.string.display_version, version.toString())
            // localDataVersionが変わる＝DBが更新されるのでviewModelが持っている全曲情報も更新する
            // 全曲情報が更新されれば画面上のリストも更新される(別画面から更新されることもある)
            fragmentViewModel.loadAllSongs()
            // TODO いるかどうか確認 128行目の修正でどうにかなってるかも
        }

        dataUpdateViewModel.updateAvailable.observe(viewLifecycleOwner) { updateAvailable ->
            switchUpdateAvailable(updateAvailable)
        }

        dataUpdateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            switchLoading(isLoading)
        }

        dataUpdateViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        binding.resetButton.setOnClickListener {
            fragmentViewModel.resetSearchWord()
        }

        binding.updateButton.setOnClickListener {
            lifecycleScope.launch {
                refreshDataAndView()
            }
        }

        binding.dataVersion.setOnClickListener {
            lifecycleScope.launch {
                dataUpdateViewModel.checkNewDataVersionAvailable(versionDataStore.getDataVersion())
            }
        }

        lifecycleScope.launch {
            val dataVersion = versionDataStore.getDataVersion()
            if (dataVersion == 0) {
                refreshDataAndView()
                fragmentViewModel.loadAllSongs()
            } else {
                dataUpdateViewModel.checkNewDataVersionAvailable(dataVersion)
            }

            setSongsToSearchedResult()
        }
    }

    private fun setSongsToSearchedResult() {
        searchedSongsAdapter.submitList(fragmentViewModel.searchSongsByName()) {
            binding.searchedSongs.scrollToPosition(0)
        }
    }

    private suspend fun refreshDataAndView() {
        dataUpdateViewModel.downloadSongData()
        dataUpdateViewModel.localDataVersion.value?.let {
            versionDataStore.saveDataVersionStore(it)
        }
    }

    private fun switchLoading(isLoading: Boolean) {
        // レイアウトを崩さないためにローディング時も Gone にしない
        binding.searchedSongs.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.loadingText.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun switchUpdateAvailable(updateAvailable: Boolean) {
        binding.dataVersion.visibility = if (updateAvailable) View.GONE else View.VISIBLE
        binding.updateButton.visibility = if (updateAvailable) View.VISIBLE else View.GONE
    }
}