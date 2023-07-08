package com.fias.ddrhighspeed.search.songsearch

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

        binding.resetButton.setOnClickListener {
            viewModel.resetSearchWord()
        }

        binding.searchedSongs.adapter = searchedSongsAdapter
        CoroutineScope(Dispatchers.IO).launch {
            handler.post {
                searchedSongsAdapter.submitList(viewModel.getNewSongs())
            }
        }

        viewModel.searchWord.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                handler.post {
                    searchedSongsAdapter.submitList(viewModel.searchSongsByName()) {
                        binding.searchedSongs.scrollToPosition(0)
                    }
                }
            }
        }

        lifecycleScope.launch {
            val localVersion = versionDataStore.getDataVersion()
            viewModel.checkNewDataVersionAvailable(localVersion)
            // checkして、データソースのバージョンのほうが新しければ更新ボタンを押せるようにする
            viewModel.result.collect { result ->
                binding.dataVersion.visibility = if (result == true) View.GONE else View.VISIBLE
                binding.updateButton.visibility = if (result == true) View.VISIBLE else View.GONE

                binding.dataVersion.text =
                    getString(R.string.display_version, localVersion.toString())
            }
        }

        // TODO :バージョンタップでバージョン確認できるようにする。または右上にメニュー

        binding.updateButton.setOnClickListener {
            // TODO visibility の切り替えメソッドを作る
            binding.searchedSongs.visibility = View.INVISIBLE
            binding.loadingText.visibility = View.VISIBLE

            lifecycleScope.launch {
                viewModel.downloadSongData(versionDataStore)
                val dataVersion = viewModel.sourceDataVersion
                binding.dataVersion.text =
                    getString(R.string.display_version, dataVersion.toString())

                binding.dataVersion.visibility = View.VISIBLE
                binding.updateButton.visibility = View.GONE

                binding.searchedSongs.visibility = View.VISIBLE
                binding.loadingText.visibility = View.GONE

                CoroutineScope(Dispatchers.IO).launch {
                    handler.post {
                        viewModel.resetSearchWord()
                        searchedSongsAdapter.submitList(viewModel.getNewSongs())
                    }
                }
            }
        }

//        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
//            if (!errorMessage.isNullOrEmpty()) {
//                // Show error message
//                AlertDialog.Builder(requireContext())
//                    .setMessage(errorMessage)
//                    .setPositiveButton("OK", null)
//                    .show()
//            }
//        })
    }
}