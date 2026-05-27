package com.gamevault.app.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamevault.app.data.model.FavoriteGame
import com.gamevault.app.databinding.ItemGameCardBinding

class FavoritesAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<FavoriteGame, FavoritesAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemGameCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemGameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val game = getItem(position)
        holder.binding.tvGameName.text = game.name
        holder.binding.tvRating.text = "★ ${String.format("%.1f", game.rating)}"
        Glide.with(holder.itemView.context)
            .load(game.backgroundImage)
            .centerCrop()
            .into(holder.binding.ivGameCover)
        holder.binding.root.setOnClickListener { onClick(game.gameId) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<FavoriteGame>() {
            override fun areItemsTheSame(a: FavoriteGame, b: FavoriteGame) = a.gameId == b.gameId
            override fun areContentsTheSame(a: FavoriteGame, b: FavoriteGame) = a == b
        }
    }
}
