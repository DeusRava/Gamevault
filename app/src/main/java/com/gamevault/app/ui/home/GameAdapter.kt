package com.gamevault.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamevault.app.R
import com.gamevault.app.data.model.Game
import com.gamevault.app.databinding.ItemGameCardBinding

class GameAdapter(
    private val onGameClick: (Game) -> Unit
) : ListAdapter<Game, GameAdapter.GameViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameViewHolder(
        private val binding: ItemGameCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.tvGameName.text = game.name
            binding.tvRating.text = String.format("★ %.1f", game.rating)
            binding.tvPlatforms.text = game.platformNames

            Glide.with(binding.root)
                .load(game.backgroundImage)
                .placeholder(R.drawable.placeholder_game)
                .centerCrop()
                .into(binding.ivGameCover)

            binding.root.setOnClickListener { onGameClick(game) }
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Game, newItem: Game) = oldItem == newItem
    }
}
