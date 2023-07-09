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
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import kotlinx.coroutines.CoroutineScope
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

    private val viewModel: EstimateByNameViewModel by viewModels {
        EstimateByNameViewModelFactory(
            (activity?.application as SongApplication).db
        )
    }

    private val versionDataStore: DataVersionDataStore by lazy {
        DataVersionDataStore(
            requireContext().versionDataStore
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
            it.fragmentViewModel = this.viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val newSongs = withContext(Dispatchers.IO) {
                viewModel.getNewSongs()
            }
            handler.post {
                searchedSongsAdapter.submitList(newSongs)
            }

            var localVersion = versionDataStore.getDataVersion()
            if (localVersion == 0) {
                downloadData(this)
                localVersion = versionDataStore.getDataVersion()
            }

            binding.dataVersion.text =
                getString(R.string.display_version, localVersion.toString())

            displayVersionOrUpdateButton(localVersion)
        }

        binding.searchedSongs.adapter = searchedSongsAdapter

        viewModel.searchWord.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                updateTableBySearchWord()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                // Show error message
                AlertDialog.Builder(requireContext())
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", null)
                    .show()
            }
        }

        // TODO :バージョンタップでバージョン確認できるようにする。または右上にメニュー

        binding.resetButton.setOnClickListener {
            viewModel.resetSearchWord()
        }

        binding.updateButton.setOnClickListener {
            lifecycleScope.launch {
                downloadData(this)
            }
        }

        binding.dataVersion.setOnClickListener {
            lifecycleScope.launch {

                displayVersionOrUpdateButton(versionDataStore.getDataVersion())
            }
        }
    }

    private suspend fun downloadData(coroutineScope: CoroutineScope) {
        displayLoading()
        viewModel.downloadSongData()
        versionDataStore.saveDataVersionStore(viewModel.sourceDataVersion)

        val dataVersion = versionDataStore.getDataVersion()
        binding.dataVersion.text =
            getString(R.string.display_version, dataVersion.toString())

        coroutineScope.launch {
            displayVersionOrUpdateButton(dataVersion)
        }
        updateTableBySearchWord()

        displaySearchResultTable()
    }

    private suspend fun displayVersionOrUpdateButton(localVersion: Int) {
        viewModel.checkNewDataVersionAvailable(localVersion)
        // checkして、データソースのバージョンのほうが新しければ更新ボタンを押せるようにする
        viewModel.result.collect { result ->
            binding.dataVersion.visibility = if (result == true) View.GONE else View.VISIBLE
            binding.updateButton.visibility = if (result == true) View.VISIBLE else View.GONE
        }
    }

    private fun displaySearchResultTable() {
        binding.searchedSongs.visibility = View.VISIBLE
        binding.loadingText.visibility = View.GONE
    }

    private fun displayLoading() {
        binding.searchedSongs.visibility = View.INVISIBLE
        binding.loadingText.visibility = View.VISIBLE
    }

    private suspend fun updateTableBySearchWord() {
        val songs = withContext(Dispatchers.IO) {
            // Fetch data on IO thread
            viewModel.searchSongsByName()
        }
        handler.post {
            searchedSongsAdapter.submitList(songs) {
                binding.searchedSongs.scrollToPosition(0)
            }
        }
    }
}