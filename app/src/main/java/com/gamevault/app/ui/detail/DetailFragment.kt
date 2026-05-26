package com.gamevault.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gamevault.app.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.loadGame(args.gameId)

        viewModel.game.observe(viewLifecycleOwner) { game ->
            binding.tvTitle.text = game.name
            binding.tvRating.text = "★ ${String.format("%.1f", game.rating)} (${game.ratingsCount} ratings)"
            binding.tvPlatforms.text = game.platformNames
            binding.tvGenres.text = game.genreNames
            binding.tvReleased.text = game.released ?: "Unknown"
            binding.contentGroup.visibility = View.VISIBLE

            // Use requireContext() - safe since observe only fires while fragment is alive
            Glide.with(requireContext())
                .load(game.backgroundImage)
                .centerCrop()
                .into(binding.ivHero)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            // Don't hide content_group here - it's shown only when game data arrives
            if (loading) binding.contentGroup.visibility = View.GONE
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
