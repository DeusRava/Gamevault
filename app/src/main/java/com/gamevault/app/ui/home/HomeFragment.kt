package com.gamevault.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamevault.app.R
import com.gamevault.app.data.model.PlatformType
import com.gamevault.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var popularAdapter: GameAdapter
    private lateinit var newReleasesAdapter: GameAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupChips()
        observeViewModel()
    }

    private fun setupAdapters() {
        popularAdapter = GameAdapter { game ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToDetail(game.id)
            )
        }
        newReleasesAdapter = GameAdapter { game ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToDetail(game.id)
            )
        }

        binding.rvPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopular.adapter = popularAdapter

        binding.rvNewReleases.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNewReleases.adapter = newReleasesAdapter
    }

    private fun setupChips() {
        // Use checkedChipId listener so filter triggers on every selection change,
        // including re-selecting the same chip after data changes
        binding.chipAll.setOnClickListener {
            if (binding.chipAll.isChecked) viewModel.setPlatformFilter(PlatformType.ALL)
        }
        binding.chipPc.setOnClickListener {
            if (binding.chipPc.isChecked) viewModel.setPlatformFilter(PlatformType.PC)
        }
        binding.chipMobile.setOnClickListener {
            if (binding.chipMobile.isChecked) viewModel.setPlatformFilter(PlatformType.MOBILE)
        }
        // Default selection
        binding.chipAll.isChecked = true
    }

    private fun observeViewModel() {
        viewModel.popularGames.observe(viewLifecycleOwner) { games ->
            popularAdapter.submitList(games)
        }
        viewModel.newReleases.observe(viewLifecycleOwner) { games ->
            newReleasesAdapter.submitList(games)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.tvError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.tvError.text = error
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
