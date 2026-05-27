package com.gamevault.app.data.repository

import com.gamevault.app.data.api.ItadApiService
import com.gamevault.app.data.api.SteamApiService
import com.gamevault.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PriceRepository @Inject constructor(
    private val steamApi: SteamApiService,
    private val itadApi: ItadApiService
) {
    // Key countries to compare — covers major pricing regions
    private val steamCountries = listOf(
        Triple("US", "USD", "United States"),
        Triple("GB", "GBP", "United Kingdom"),
        Triple("DE", "EUR", "Germany"),
        Triple("TR", "TRY", "Turkey"),
        Triple("AR", "ARS", "Argentina"),
        Triple("RU", "RUB", "Russia"),
        Triple("BR", "BRL", "Brazil"),
        Triple("IN", "INR", "India"),
        Triple("AU", "AUD", "Australia"),
        Triple("JP", "JPY", "Japan"),
        Triple("KZ", "KZT", "Kazakhstan"),
        Triple("UA", "UAH", "Ukraine"),
        Triple("PL", "PLN", "Poland"),
        Triple("CN", "CNY", "China"),
        Triple("MX", "MXN", "Mexico")
    )

    /**
     * Fetch Steam prices for a given appId across all key regions.
     */
    suspend fun getSteamPrices(appId: String): Result<List<GamePrice>> = runCatching {
        val prices = mutableListOf<GamePrice>()
        for ((cc, currency, countryName) in steamCountries) {
            runCatching {
                val response = steamApi.getAppPrice(appIds = appId, countryCode = cc)
                val appData = response[appId]
                if (appData?.success == true) {
                    val po = appData.data?.priceOverview
                    if (po != null) {
                        prices += GamePrice(
                            store = Store.STEAM,
                            countryCode = cc,
                            countryName = countryName,
                            currency = po.currency,
                            amount = po.final / 100.0,
                            isFree = false,
                            storeUrl = "https://store.steampowered.com/app/$appId"
                        )
                    } else {
                        // free-to-play or not available
                        prices += GamePrice(
                            store = Store.STEAM,
                            countryCode = cc,
                            countryName = countryName,
                            currency = currency,
                            amount = 0.0,
                            isFree = true,
                            storeUrl = "https://store.steampowered.com/app/$appId"
                        )
                    }
                }
            }
            // Small delay to be polite to Steam's API
            kotlinx.coroutines.delay(200)
        }
        prices
    }

    /**
     * Fetch EGS price via ITAD for a given game name.
     * ITAD has real EGS pricing data.
     */
    suspend fun getEgsPrices(gameName: String): Result<List<GamePrice>> = runCatching {
        val response = itadApi.searchPrices(query = gameName)
        val prices = mutableListOf<GamePrice>()
        val game = response.list.firstOrNull() ?: return@runCatching prices
        val egsDeal = game.deals.firstOrNull { it.shop.id == ItadApiService.EGS_SHOP_ID }
        if (egsDeal != null) {
            prices += GamePrice(
                store = Store.EGS,
                countryCode = "US",
                countryName = "United States",
                currency = "USD",
                amount = egsDeal.priceNew,
                isFree = egsDeal.priceNew == 0.0,
                storeUrl = egsDeal.url
            )
        }
        prices
    }

    /**
     * Build a full PriceComparison for a game.
     * steamAppId is optional — if null only EGS is queried.
     */
    suspend fun getPriceComparison(
        gameName: String,
        steamAppId: String?
    ): Result<PriceComparison> = runCatching {
        val allPrices = mutableListOf<GamePrice>()

        if (steamAppId != null) {
            getSteamPrices(steamAppId).getOrNull()?.let { allPrices.addAll(it) }
        }
        getEgsPrices(gameName).getOrNull()?.let { allPrices.addAll(it) }

        PriceComparison(
            gameName = gameName,
            steamAppId = steamAppId,
            prices = allPrices
        )
    }
}
