package com.gamevault.app.data.model

import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("background_image") val backgroundImage: String?,
    val rating: Double,
    @SerializedName("ratings_count") val ratingsCount: Int,
    val released: String?,
    val platforms: List<PlatformWrapper>?,
    val genres: List<Genre>?,
    @SerializedName("short_screenshots") val screenshots: List<Screenshot>?,
    val stores: List<StoreWrapper>? = null,         // included in detail endpoint
    val description: String? = null                  // included in detail endpoint
) {
    val platformNames: String
        get() = platforms?.joinToString(", ") { it.platform.name } ?: "Unknown"

    val genreNames: String
        get() = genres?.joinToString(", ") { it.name } ?: ""

    val platformType: PlatformType
        get() {
            val names = platforms?.map { it.platform.name.lowercase() } ?: return PlatformType.ALL
            return when {
                names.any { it.contains("android") || it.contains("ios") || it.contains("mobile") } &&
                        !names.any { it.contains("pc") || it.contains("playstation") || it.contains("xbox") || it.contains("nintendo") }
                -> PlatformType.MOBILE
                else -> PlatformType.PC
            }
        }

    /** Extract Steam appId from RAWG stores list */
    val steamAppId: String?
        get() {
            val steamStore = stores?.find { it.store.slug == "steam" }
            val url = steamStore?.url ?: return null
            // URL format: https://store.steampowered.com/app/1234/
            val match = Regex("/app/(\\d+)").find(url)
            return match?.groupValues?.get(1)
        }
}

data class PlatformWrapper(val platform: Platform)

data class Platform(val id: Int, val name: String, val slug: String)

data class Genre(val id: Int, val name: String, val slug: String)

data class Screenshot(val id: Int, val image: String)

data class StoreWrapper(
    val id: Int,
    val store: StoreInfo,
    val url: String
)

data class StoreInfo(
    val id: Int,
    val name: String,
    val slug: String
)

data class GamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

enum class PlatformType { ALL, PC, MOBILE }
