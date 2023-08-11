package com.fias.ddrhighspeed.search.songsearch

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.shared.cache.SongIndex
import com.fias.ddrhighspeed.shared.spreadsheet.SpreadSheetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val version_PREFERENCES_NAME = "version_preferences"
val Context.versionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = version_PREFERENCES_NAME
)

class EstimateByNameFragment : Fragment() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val spreadSheetService = SpreadSheetService()
    private val viewModel: EstimateByNameViewModel by viewModels {
        EstimateByNameViewModelFactory(
            (activity?.application as SongApplication).db, spreadSheetService
        )
    }

    private val versionDataStore: DataVersionDataStore by lazy {
        DataVersionDataStore(
            requireContext().versionDataStore
        )
    }

    private val searchedSongsAdapter: SearchedSongsAdapter by lazy {
        val clickListener = ClickSongListener { song: SongIndex ->
            val navController = findNavController()
            val action =
                ScrollSpeedBoardFragmentDirections.actionSongSearchToSongDetailPager(song.id)
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
            it.fragmentViewModel = this.viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val newSongs = withContext(Dispatchers.IO) {
                viewModel.getNewSongIndice()
            }
            handler.post {
                searchedSongsAdapter.submitList(newSongs)
            }

            val dataVersion = versionDataStore.getDataVersion()
            if (dataVersion == 0) {
                refreshDataAndView()
            } else {
                viewModel.checkNewDataVersionAvailable(dataVersion)
            }
        }

        binding.searchedSongs.adapter = searchedSongsAdapter

        viewModel.searchWord.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                updateTableBySearchWord()
            }
        }

        viewModel.localDataVersion.observe(viewLifecycleOwner) { version ->
            binding.dataVersion.text = getString(R.string.display_version, version.toString())
            lifecycleScope.launch {
            }
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
    }

    private suspend fun refreshDataAndView() {
        viewModel.downloadSongData()
        viewModel.localDataVersion.value?.let {
            versionDataStore.saveDataVersionStore(it)
        }

        updateTableBySearchWord()
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

    private suspend fun updateTableBySearchWord() {
        val songs = withContext(Dispatchers.IO) {
            viewModel.searchSongsByName()
        }
        handler.post {
            searchedSongsAdapter.submitList(songs) {
                binding.searchedSongs.scrollToPosition(0)
            }
        }
    }
}