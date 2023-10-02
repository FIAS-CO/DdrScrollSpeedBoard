package com.fias.ddrhighspeed.search.coursedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedFragmentBase
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.database.SongApplication
import com.fias.ddrhighspeed.databinding.FragmentCourseDetailBinding
import com.fias.ddrhighspeed.view.MarqueeToolbar
import com.google.android.material.textfield.TextInputEditText

class CourseDetailFragment : ScrollSpeedFragmentBase() {
    override val scrollSpeedTextBox: TextInputEditText
        get() = binding.textInputEditText

    private var _fragmentBinding: FragmentCourseDetailBinding? = null
    private val binding get() = _fragmentBinding!!
    private val detailBoardAdapter: CourseDetailBoardAdapter by lazy { CourseDetailBoardAdapter() }
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
    }

    private fun SongPropertyInCourseTable.setStaticData(
        firstSong: SongData,
        @StringRes resId: Int
    ) {
        this.apply {
            setSongLabel(
                getString(
                    resId,
                    firstSong.name
                )
            )
            setTopRightText("基本BPM: ${firstSong.baseBpm}")
            setBottomRightText("ゲーム内表示BPM: ${firstSong.displayBpm}")
        }
    }

    private fun SongPropertyInCourseTable.setHighSpeed(songIndex: Int) {
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
}