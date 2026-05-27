package com.gamevault.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gamevault.app.databinding.FragmentDetailBinding
import com.gamevault.app.data.model.Store
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var priceAdapter: PriceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        priceAdapter = PriceAdapter()
        binding.rvPrices.layoutManager = LinearLayoutManager(context)
        binding.rvPrices.adapter = priceAdapter

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }

        setupStoreChips()
        viewModel.loadGame(args.gameId)
        observeViewModel()
    }

    private fun setupStoreChips() {
        binding.chipAllStores.setOnClickListener {
            if (binding.chipAllStores.isChecked) {
                viewModel.setStoreFilter(null)
                refreshPriceList()
            }
        }
        binding.chipSteam.setOnClickListener {
            if (binding.chipSteam.isChecked) {
                viewModel.setStoreFilter(Store.STEAM)
                refreshPriceList()
            }
        }
        binding.chipEgs.setOnClickListener {
            if (binding.chipEgs.isChecked) {
                viewModel.setStoreFilter(Store.EGS)
                refreshPriceList()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.game.observe(viewLifecycleOwner) { game ->
            binding.tvTitle.text = game.name
            binding.tvRating.text = "★ ${String.format("%.1f", game.rating)} (${game.ratingsCount} ratings)"
            binding.tvPlatforms.text = game.platformNames
            binding.tvGenres.text = game.genreNames
            binding.tvReleased.text = game.released ?: "Unknown"
            binding.contentGroup.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(game.backgroundImage)
                .centerCrop()
                .into(binding.ivHero)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) binding.contentGroup.visibility = View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.tvError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.tvError.text = error
        }

        viewModel.pricesLoading.observe(viewLifecycleOwner) { loading ->
            binding.pricesLoadingGroup.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.priceComparison.observe(viewLifecycleOwner) { comparison ->
            if (comparison == null) return@observe
            val best = comparison.bestDeal
            if (best != null) {
                binding.cardBestDeal.visibility = View.VISIBLE
                binding.tvBestCountry.text = "${best.countryName} via ${best.store.displayName}"
                binding.tvBestPrice.text = "${best.currency} ${String.format("%.2f", best.amount)}"
                binding.tvBestStore.text = "Lowest price across all regions"
                priceAdapter.bestDealKey = "${best.countryCode}|${best.store.name}"
            } else {
                binding.cardBestDeal.visibility = View.GONE
            }
            refreshPriceList()
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { fav ->
            binding.btnFavorite.setImageResource(
                if (fav) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )
        }
    }

    private fun refreshPriceList() {
        val prices = viewModel.filteredPrices()
        if (prices.isEmpty()) {
            binding.tvNoPrices.visibility = View.VISIBLE
            binding.rvPrices.visibility = View.GONE
        } else {
            binding.tvNoPrices.visibility = View.GONE
            binding.rvPrices.visibility = View.VISIBLE
            priceAdapter.submitList(prices)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
