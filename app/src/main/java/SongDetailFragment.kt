import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.DetailBoardAdapter
import com.fias.ddrhighspeed.R
import com.fias.ddrhighspeed.ScrollSpeedBoardViewModel
import com.fias.ddrhighspeed.SongDetailViewModel
import com.fias.ddrhighspeed.databinding.FragmentSongDetailBinding

class SongDetailFragment : Fragment() {
    private var _fragmentBinding: FragmentSongDetailBinding? = null
    private val binding get() = _fragmentBinding!!
    private val viewModel: SongDetailViewModel by activityViewModels()
    private val detailBoardAdapter: DetailBoardAdapter by lazy { DetailBoardAdapter() }
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()

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
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.songDetailList.apply {
            adapter = detailBoardAdapter
        }
        val list = viewModel.createRows(sharedViewModel.getScrollSpeedValue(), viewModel.song)
        detailBoardAdapter.submitList(list)

        val scrollSpeedObserver = Observer<String> {
            sharedViewModel.longPushButtonCommand {
                val scrollSpeedValue = sharedViewModel.getScrollSpeedValue()
                val list2 = viewModel.createRows(scrollSpeedValue, viewModel.song)
                detailBoardAdapter.submitList(list2)
            }
        }
        sharedViewModel.scrollSpeed.observe(viewLifecycleOwner, scrollSpeedObserver)
    }
}