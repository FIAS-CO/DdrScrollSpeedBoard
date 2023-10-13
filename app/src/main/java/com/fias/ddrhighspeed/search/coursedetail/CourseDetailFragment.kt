package com.fias.ddrhighspeed.search.coursedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedFragmentBase
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentCourseDetailBinding
import com.fias.ddrhighspeed.view.AdViewUtil
import com.fias.ddrhighspeed.view.MarqueeToolbar
import com.google.android.material.textfield.TextInputEditText

class CourseDetailFragment : ScrollSpeedFragmentBase() {
    override val scrollSpeedTextBox: TextInputEditText
        get() = binding.textInputEditText

    private var _fragmentBinding: FragmentCourseDetailBinding? = null
    private val binding get() = _fragmentBinding!!
    private val viewModel: CourseDetailViewModel by viewModels() {
        CourseDetailViewModelFactory(
            (activity?.application as SongApplication).db
        )
    }
    private val args: CourseDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentCourseDetailBinding?>(
            inflater,
            R.layout.fragment_course_detail,
            container,
            false
        ).also {
            it.fragmentViewModel = this.viewModel
            it.viewModel = this.sharedViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        viewModel.course = args.course

        val toolbar = requireActivity().findViewById<MarqueeToolbar>(R.id.toolbar)
        toolbar.title = viewModel.course.getCourseLabel()
        toolbar.startMarqueeScroll()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incrementUp.setSpinButtonListener(sharedViewModel.countUp)
        binding.incrementDown.setSpinButtonListener(sharedViewModel.countDown)

        binding.firstSongLabel.text =
            getString(R.string.song_name_in_course_1st, viewModel.firstSong.name)

        binding.firstSongView.setStaticData(viewModel.firstSong, R.string.song_name_in_course_1st)
        binding.secondSongView.setStaticData(viewModel.secondSong, R.string.song_name_in_course_2nd)
        binding.thirdSongView.setStaticData(viewModel.thirdSong, R.string.song_name_in_course_3rd)
        binding.fourthSongView.setStaticData(viewModel.fourthSong, R.string.song_name_in_course_4th)

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                binding.firstSongView.setHighSpeed(1)
                binding.secondSongView.setHighSpeed(2)
                binding.thirdSongView.setHighSpeed(3)
                binding.fourthSongView.setHighSpeed(4)
                onScrollSpeedChange()
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)

        AdViewUtil().loadAdView(binding.adView, requireContext())
    }

    private fun SongInCourseTable.setStaticData(
        song: SongData,
        @StringRes resId: Int
    ) {
        this.apply {
            setSongLabel(getString(resId, song.nameWithDifficultyLabel()))
            setBottomLeftText("基本BPM: ${song.baseBpm}")
            setBottomCenterText("高速地帯: ${if (song.hasHighSpeedArea()) "◯" else "✕"}")
            setBottomRightText("低速地帯: ${if (song.hasLowSpeedArea()) "◯" else "✕"}")
            setButtonOnClick { goToSongDetail(song) }
        }
    }

    private fun SongInCourseTable.setHighSpeed(songIndex: Int) {
        this.apply {
            setLeftText(
                "推奨ハイスピ:${
                    viewModel.calculate(
                        sharedViewModel.getScrollSpeedValue(),
                        songIndex
                    )
                }"
            )
        }
    }

    private fun goToSongDetail(song: SongData) {
        val navController = findNavController()
        val action =
            CourseDetailFragmentDirections.actionCourseDetailToSongDetailPager(song)
        navController.navigate(action)
    }
}