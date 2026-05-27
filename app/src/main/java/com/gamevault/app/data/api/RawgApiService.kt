package com.gamevault.app.data.api

import com.gamevault.app.data.model.Game
import com.gamevault.app.data.model.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("ordering") ordering: String = "-rating",
        @Query("platforms") platforms: String? = null,
        @Query("genres") genres: String? = null
    ): GamesResponse

    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String,
        @Query("search") query: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("platforms") platforms: String? = null,
        @Query("search_precise") searchPrecise: Boolean = false
    ): GamesResponse

    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): Game

    companion object {
        const val BASE_URL = "https://api.rawg.io/api/"
        // Platform IDs from RAWG
        // PC = 4, PlayStation 5 = 187, Xbox Series X = 186
        // iOS = 3, Android = 21, macOS = 5, Linux = 6
        const val PC_PLATFORMS = "4,187,186,1,14,80"       // PC, PS5, Xbox, PS4, Xbox One, PS3
        const val MOBILE_PLATFORMS = "3,21"                 // iOS, Android
    }
}
