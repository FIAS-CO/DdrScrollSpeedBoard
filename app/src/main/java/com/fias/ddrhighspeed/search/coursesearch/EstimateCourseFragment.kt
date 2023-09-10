package com.fias.ddrhighspeed.search.coursesearch

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedBoardFragmentDirections
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.search.songsearch.EstimateByNameViewModel
import com.fias.ddrhighspeed.shared.spreadsheet.SpreadSheetService
import kotlinx.coroutines.launch

private const val version_PREFERENCES_NAME = "version_preferences"
val Context.versionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = version_PREFERENCES_NAME
)

class EstimateCourseFragment : Fragment() {
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val spreadSheetService = SpreadSheetService()
    private val viewModel: EstimateByNameViewModel by viewModels {
        EstimateCourseViewModelFactory(
            (activity?.application as SongApplication).db, spreadSheetService
        )
    }

    private val versionDataStore: DataVersionDataStore by lazy {
        DataVersionDataStore(
            requireContext().versionDataStore
        )
    }

    private val searchedCoursesAdapter: SearchedCoursesAdapter by lazy {
        val clickListener = ClickSongListener { song: SongData ->
            val navController = findNavController()
            val action =
                ScrollSpeedBoardFragmentDirections.actionSongSearchToSongDetailPager(song)
            navController.navigate(action)
        }
        SearchedCoursesAdapter(clickListener)
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
        binding.searchedSongs.adapter = searchedCoursesAdapter

        viewModel.searchWord.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        viewModel.baseSongDataList.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        viewModel.localDataVersion.observe(viewLifecycleOwner) { version ->
            binding.dataVersion.text = getString(R.string.display_version, version.toString())
        }

        viewModel.updateAvailable.observe(viewLifecycleOwner) { updateAvailable ->
            switchUpdateAvailable(updateAvailable)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            switchLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                AlertDialog.Builder(requireContext())
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetSearchWord()
        }

        binding.updateButton.setOnClickListener {
            lifecycleScope.launch {
                refreshDataAndView()
            }
        }

        binding.dataVersion.setOnClickListener {
            lifecycleScope.launch {
                viewModel.checkNewDataVersionAvailable(versionDataStore.getDataVersion())
            }
        }

        lifecycleScope.launch {
            val dataVersion = versionDataStore.getDataVersion()
            if (dataVersion == 0) {
                refreshDataAndView()
            } else {
                viewModel.checkNewDataVersionAvailable(dataVersion)
                setSongsToSearchedResult()
            }
        }
    }

    private fun setSongsToSearchedResult() {
        searchedCoursesAdapter.submitList(viewModel.searchSongsByName()) {
            binding.searchedSongs.scrollToPosition(0)
        }
    }

    private suspend fun refreshDataAndView() {
        viewModel.downloadSongData()
        viewModel.localDataVersion.value?.let {
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