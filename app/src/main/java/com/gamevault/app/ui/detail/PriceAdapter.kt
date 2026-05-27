package com.gamevault.app.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gamevault.app.R
import com.gamevault.app.data.model.GamePrice

class PriceAdapter : ListAdapter<GamePrice, PriceAdapter.VH>(DIFF) {

    var bestDealKey: String? = null   // "countryCode|store" of the best deal

    private val countryFlags = mapOf(
        "US" to "🇺🇸", "GB" to "🇬🇧", "DE" to "🇩🇪", "TR" to "🇹🇷",
        "AR" to "🇦🇷", "RU" to "🇷🇺", "BR" to "🇧🇷", "IN" to "🇮🇳",
        "AU" to "🇦🇺", "JP" to "🇯🇵", "KZ" to "🇰🇿", "UA" to "🇺🇦",
        "PL" to "🇵🇱", "CN" to "🇨🇳", "MX" to "🇲🇽"
    )

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCountry: TextView = itemView.findViewById(R.id.tv_country)
        val tvStore: TextView = itemView.findViewById(R.id.tv_store)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvBadge: TextView = itemView.findViewById(R.id.tv_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_price_row, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val price = getItem(position)
        val flag = countryFlags[price.countryCode] ?: "🌐"
        holder.tvCountry.text = "$flag ${price.countryName}"
        holder.tvStore.text = price.store.displayName
        holder.tvPrice.text = if (price.isFree) "Free" else
            "${price.currency} ${String.format("%.2f", price.amount)}"

        val key = "${price.countryCode}|${price.store.name}"
        val isBest = key == bestDealKey && !price.isFree
        holder.tvBadge.visibility = if (isBest) View.VISIBLE else View.GONE

        // Highlight best deal row text
        val textColor = if (isBest) 0xFF4CAF50.toInt() else 0xFFFFFFFF.toInt()
        holder.tvPrice.setTextColor(textColor)
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<GamePrice>() {
            override fun areItemsTheSame(a: GamePrice, b: GamePrice) =
                a.countryCode == b.countryCode && a.store == b.store
            override fun areContentsTheSame(a: GamePrice, b: GamePrice) = a == b
        }
    }
}
