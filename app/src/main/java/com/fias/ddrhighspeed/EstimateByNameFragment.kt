package com.fias.ddrhighspeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.fias.ddrhighspeed.databinding.FragmentEstimateByNameBinding
import com.fias.ddrhighspeed.model.ResultRow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EstimateByNameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EstimateByNameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _fragmentBinding: FragmentEstimateByNameBinding? = null
    private val binding get() = _fragmentBinding!!
    private val sharedViewModel: ScrollSpeedBoardViewModel by activityViewModels()

    private lateinit var searchedSongsAdapter: SearchedSongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            it.boardViewModel = this.sharedViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchedSongsAdapter = SearchedSongsAdapter()

        binding.searchedSongs.apply {
            adapter = searchedSongsAdapter
        }

        val searchWordObserver = Observer<String> {
            searchedSongsAdapter.submitList(
                listOf(
                    ResultRow("曲1", "", ""),
                    ResultRow("曲2", "", ""),
                    ResultRow("曲3", "", ""),
                )
            )
        }
        sharedViewModel.searchWord.observe(viewLifecycleOwner, searchWordObserver)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EstimateByNameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EstimateByNameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}