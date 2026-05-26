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
    @SerializedName("short_screenshots") val screenshots: List<Screenshot>?
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
}

data class PlatformWrapper(
    val platform: Platform
)

data class Platform(
    val id: Int,
    val name: String,
    val slug: String
)

data class Genre(
    val id: Int,
    val name: String,
    val slug: String
)

data class Screenshot(
    val id: Int,
    val image: String
)

data class GamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

enum class PlatformType {
    ALL, PC, MOBILE
}
