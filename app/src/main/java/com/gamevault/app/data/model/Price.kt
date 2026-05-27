package com.gamevault.app.data.model

import com.google.gson.annotations.SerializedName

// ── Price model ──────────────────────────────────────────────────────────────

data class GamePrice(
    val store: Store,
    val countryCode: String,
    val countryName: String,
    val currency: String,
    val amount: Double,
    val isFree: Boolean = false,
    val storeUrl: String = ""
)

enum class Store(val displayName: String) {
    STEAM("Steam"),
    EGS("Epic Games"),
    GOOGLE_PLAY("Google Play"),
    APP_STORE("App Store")
}

data class PriceComparison(
    val gameName: String,
    val steamAppId: String?,
    val prices: List<GamePrice>,
    val bestDeal: GamePrice? = prices.filterNot { it.isFree }.minByOrNull { it.amount }
)

// ── Steam API models ──────────────────────────────────────────────────────────

data class SteamPriceData(
    val currency: String,
    val initial: Int,
    val final: Int,
    @SerializedName("discount_percent") val discountPercent: Int,
    @SerializedName("final_formatted") val finalFormatted: String
)

data class SteamPriceOverview(
    @SerializedName("price_overview") val priceOverview: SteamPriceData?
)

data class SteamAppData(
    val success: Boolean,
    val data: SteamPriceOverview?
)

// ── ITAD (IsThereAnyDeal) models for EGS ─────────────────────────────────────

data class ItadPriceResponse(
    val list: List<ItadGame>
)

data class ItadGame(
    val id: String,
    val slug: String,
    @SerializedName("title") val title: String,
    val deals: List<ItadDeal>
)

data class ItadDeal(
    val shop: ItadShop,
    @SerializedName("price_new") val priceNew: Double,
    @SerializedName("price_old") val priceOld: Double,
    @SerializedName("price_cut") val priceCut: Int,
    val url: String
)

data class ItadShop(
    val id: String,
    val name: String
)
