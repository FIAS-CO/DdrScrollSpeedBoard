package com.fias.ddrhighspeed.search.songdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.SongData
import com.fias.ddrhighspeed.databinding.FragmentSongDetailPagerBinding
import com.fias.ddrhighspeed.search.songdetail.movie.SongMovieFragment
import com.google.android.material.tabs.TabLayoutMediator

class SongDetailPagerFragment : Fragment() {
    private var _fragmentBinding: FragmentSongDetailPagerBinding? = null
    private val binding get() = _fragmentBinding!!

    private lateinit var songDetailTabAdapter: SongDetailTabAdapter
    private lateinit var viewPager: ViewPager2
    private val tabTitleArray = arrayOf(
        "ハイスピ計算",
        "動画",
    )
    private val args: SongDetailPagerFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = DataBindingUtil.inflate<FragmentSongDetailPagerBinding?>(
            inflater,
            R.layout.fragment_song_detail_pager,
            container,
            false
        ).also {
            it.lifecycleOwner = this.viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songDetailTabAdapter = SongDetailTabAdapter(this, args.song)
        viewPager = binding.songDetailPager
        viewPager.adapter = songDetailTabAdapter

        val tabLayout = binding.songDetailTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }
}

class SongDetailTabAdapter(fragment: Fragment, private val songData: SongData) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongDetailFragment(songData)
            1 -> SongMovieFragment(songData.id)
            else -> throw IndexOutOfBoundsException()
        }
    }
}