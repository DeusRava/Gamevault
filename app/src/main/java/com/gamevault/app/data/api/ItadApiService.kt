package com.gamevault.app.data.api

import com.gamevault.app.data.model.ItadPriceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItadApiService {

    /**
     * Search for game prices across stores including EGS.
     * IsThereAnyDeal free API — no key required for basic search.
     */
    @GET("v01/search/search/")
    suspend fun searchPrices(
        @Query("q") query: String,
        @Query("limit") limit: Int = 3,
        @Query("region") region: String = "us",
        @Query("country") country: String = "US",
        @Query("shops") shops: String = "61"   // 61 = Epic Games Store
    ): ItadPriceResponse

    companion object {
        const val BASE_URL = "https://api.isthereanydeal.com/"
        const val EGS_SHOP_ID = "61"
    }
}
