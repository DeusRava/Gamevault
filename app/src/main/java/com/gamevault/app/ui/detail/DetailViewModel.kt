package com.gamevault.app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.app.data.model.Game
import com.gamevault.app.data.model.GamePrice
import com.gamevault.app.data.model.PriceComparison
import com.gamevault.app.data.model.Store
import com.gamevault.app.data.repository.FavoriteRepository
import com.gamevault.app.data.repository.GameRepository
import com.gamevault.app.data.repository.PriceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: GameRepository,
    private val priceRepository: PriceRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _priceComparison = MutableLiveData<PriceComparison?>()
    val priceComparison: LiveData<PriceComparison?> = _priceComparison

    private val _pricesLoading = MutableLiveData<Boolean>(false)
    val pricesLoading: LiveData<Boolean> = _pricesLoading

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    // Selected store filter for price table
    private val _selectedStore = MutableLiveData<Store?>(null)
    val selectedStore: LiveData<Store?> = _selectedStore

    fun loadGame(gameId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getGameDetail(gameId)
                .onSuccess { game ->
                    _game.value = game
                    _isFavorite.value = favoriteRepository.isFavorite(gameId)
                    loadPrices(game)
                }
                .onFailure { _error.value = it.message ?: "Failed to load game" }
            _isLoading.value = false
        }
    }

    private fun loadPrices(game: Game) {
        viewModelScope.launch {
            _pricesLoading.value = true
            // Try to extract Steam appId from RAWG stores data — fallback to name search
            val steamAppId = extractSteamAppId(game)
            priceRepository.getPriceComparison(game.name, steamAppId)
                .onSuccess { _priceComparison.value = it }
                .onFailure { /* prices unavailable — don't block detail */ }
            _pricesLoading.value = false
        }
    }

    fun toggleFavorite() {
        val game = _game.value ?: return
        viewModelScope.launch {
            favoriteRepository.toggle(game)
            _isFavorite.value = favoriteRepository.isFavorite(game.id)
        }
    }

    fun setStoreFilter(store: Store?) {
        _selectedStore.value = store
    }

    fun filteredPrices(): List<GamePrice> {
        val pc = _priceComparison.value ?: return emptyList()
        val store = _selectedStore.value
        return if (store == null) pc.prices else pc.prices.filter { it.store == store }
    }

    /** Extract Steam appId from RAWG's stores list if present */
    private fun extractSteamAppId(game: Game): String? {
        // RAWG Game detail includes a stores list — but our lightweight model doesn't fetch it.
        // We fall back to using RAWG's slug to search SteamSpy.
        // For now return null and let name-based EGS lookup handle EGS.
        // A full implementation would call RAWG /games/{id}/stores and parse the Steam URL.
        return game.steamAppId
    }
}
