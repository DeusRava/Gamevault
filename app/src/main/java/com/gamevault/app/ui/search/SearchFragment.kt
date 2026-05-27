package com.gamevault.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gamevault.app.data.model.PlatformType
import com.gamevault.app.databinding.FragmentSearchBinding
import com.gamevault.app.ui.home.GameAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: GameAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GameAdapter { game ->
            findNavController().navigate(
                SearchFragmentDirections.actionSearchToDetail(game.id)
            )
        }

        binding.rvResults.layoutManager = GridLayoutManager(context, 2)
        binding.rvResults.adapter = adapter

        binding.etSearch.addTextChangedListener { text ->
            viewModel.search(text.toString())
        }

        binding.chipAll.isChecked = true
        binding.chipAll.setOnClickListener {
            if (binding.chipAll.isChecked) {
                viewModel.setPlatformFilter(PlatformType.ALL)
                viewModel.search(binding.etSearch.text.toString())
            }
        }
        binding.chipPc.setOnClickListener {
            if (binding.chipPc.isChecked) {
                viewModel.setPlatformFilter(PlatformType.PC)
                viewModel.search(binding.etSearch.text.toString())
            }
        }
        binding.chipMobile.setOnClickListener {
            if (binding.chipMobile.isChecked) {
                viewModel.setPlatformFilter(PlatformType.MOBILE)
                viewModel.search(binding.etSearch.text.toString())
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { games ->
            adapter.submitList(games)
            val hasQuery = binding.etSearch.text?.isNotEmpty() == true
            binding.tvEmpty.visibility =
                if (games.isEmpty() && hasQuery) View.VISIBLE else View.GONE
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) binding.tvEmpty.visibility = View.GONE  // hide while searching
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
