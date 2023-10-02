package com.fias.ddrhighspeed.search.coursesearch

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
import com.fias.ddrhighspeed.CourseData
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedBoardFragmentDirections
import com.fias.ddrhighspeed.data.DataVersionDataStore
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentEstimateByCourseBinding
import com.fias.ddrhighspeed.search.DataUpdateViewModel
import com.fias.ddrhighspeed.search.DataUpdateViewModelFactory
import com.fias.ddrhighspeed.shared.spreadsheet.SpreadSheetService
import kotlinx.coroutines.launch

class EstimateCourseFragment : Fragment() {
    private var _fragmentBinding: FragmentEstimateByCourseBinding? = null
    private val binding get() = _fragmentBinding!!
    private val spreadSheetService = SpreadSheetService()
    private val fragmentViewModel: EstimateCourseViewModel by viewModels {
        EstimateCourseViewModelFactory(
            (activity?.application as SongApplication).db
        )
    }
    private val dataUpdateViewModel: DataUpdateViewModel by activityViewModels {
        DataUpdateViewModelFactory(
            (activity?.application as SongApplication).db,
            spreadSheetService,
            DataVersionDataStore(
                (requireActivity().application as SongApplication).versionDataStore
            )
        )
    }

    private val searchedCoursesAdapter: SearchedCoursesAdapter by lazy {
        val clickListener = ClickCourseListener { course: CourseData ->
            val navController = findNavController()
            val action =
                ScrollSpeedBoardFragmentDirections.actionCourseSearchToCourseDetail(course)
            navController.navigate(action)
        }
        SearchedCoursesAdapter(clickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentEstimateByCourseBinding?>(
            inflater,
            R.layout.fragment_estimate_by_course,
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
        binding.searchedSongs.adapter = searchedCoursesAdapter

        fragmentViewModel.searchWord.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        fragmentViewModel.baseCourseDataList.observe(viewLifecycleOwner) {
            setSongsToSearchedResult()
        }

        dataUpdateViewModel.localDataVersion.observe(viewLifecycleOwner) { version ->
            binding.dataVersion.text = getString(R.string.display_version, version.toString())

            // localDataVersionが変わる＝DBが更新されるのでviewModelが持っている全曲情報も更新する
            // 全曲情報が更新されれば画面上のリストも更新される(別画面から更新されることもある)
            fragmentViewModel.loadAllCourses()
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
                dataUpdateViewModel.errorMessage.value = ""
            }
        }

        binding.resetButton.setOnClickListener {
            fragmentViewModel.resetSearchWord()
        }

        binding.updateButton.setOnClickListener {
            lifecycleScope.launch {
                dataUpdateViewModel.downloadSongData()
            }
        }

        binding.dataVersion.setOnClickListener {
            lifecycleScope.launch {
                dataUpdateViewModel.checkNewDataVersionAvailable()
            }
        }

        // 画面起動時の動作
        dataUpdateViewModel.isLoading.value?.let { switchLoading(it) }
    }

    private fun setSongsToSearchedResult() {
        searchedCoursesAdapter.submitList(fragmentViewModel.searchSongsByCourse()) {
            binding.searchedSongs.scrollToPosition(0)
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