package com.gamevault.app.data.api

import com.gamevault.app.data.model.SteamAppData
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApiService {

    /**
     * Get price overview for an app in a specific country.
     * cc = country code (ISO 3166-1 alpha-2), e.g. "US", "DE", "TR"
     * filters=price_overview returns only price data (smaller payload)
     */
    @GET("appdetails")
    suspend fun getAppPrice(
        @Query("appids") appIds: String,
        @Query("cc") countryCode: String,
        @Query("filters") filters: String = "price_overview"
    ): Map<String, SteamAppData>

    companion object {
        const val BASE_URL = "https://store.steampowered.com/api/"
    }
}
