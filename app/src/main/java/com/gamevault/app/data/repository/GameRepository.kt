package com.gamevault.app.data.repository

import com.gamevault.app.BuildConfig
import com.gamevault.app.data.api.RawgApiService
import com.gamevault.app.data.model.Game
import com.gamevault.app.data.model.GamesResponse
import com.gamevault.app.data.model.PlatformType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val api: RawgApiService
) {
    private val apiKey = BuildConfig.RAWG_API_KEY

    suspend fun getPopularGames(platformType: PlatformType = PlatformType.ALL): Result<GamesResponse> {
        return runCatching {
            val platforms = when (platformType) {
                PlatformType.PC -> RawgApiService.PC_PLATFORMS
                PlatformType.MOBILE -> RawgApiService.MOBILE_PLATFORMS
                PlatformType.ALL -> null
            }
            api.getGames(apiKey = apiKey, ordering = "-rating", platforms = platforms)
        }
    }

    suspend fun getNewReleases(platformType: PlatformType = PlatformType.ALL): Result<GamesResponse> {
        return runCatching {
            val platforms = when (platformType) {
                PlatformType.PC -> RawgApiService.PC_PLATFORMS
                PlatformType.MOBILE -> RawgApiService.MOBILE_PLATFORMS
                PlatformType.ALL -> null
            }
            api.getGames(apiKey = apiKey, ordering = "-released", platforms = platforms)
        }
    }

    suspend fun searchGames(
        query: String,
        platformType: PlatformType = PlatformType.ALL
    ): Result<GamesResponse> {
        return runCatching {
            val platforms = when (platformType) {
                PlatformType.PC -> RawgApiService.PC_PLATFORMS
                PlatformType.MOBILE -> RawgApiService.MOBILE_PLATFORMS
                PlatformType.ALL -> null
            }
            api.searchGames(apiKey = apiKey, query = query, platforms = platforms)
        }
    }

    suspend fun getGameDetail(gameId: Int): Result<Game> {
        return runCatching {
            api.getGameDetail(gameId = gameId, apiKey = apiKey)
        }
    }
}
