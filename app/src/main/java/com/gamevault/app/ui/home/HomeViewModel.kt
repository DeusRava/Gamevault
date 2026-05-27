package com.gamevault.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.app.data.model.Game
import com.gamevault.app.data.model.PlatformType
import com.gamevault.app.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _popularGames = MutableLiveData<List<Game>>()
    val popularGames: LiveData<List<Game>> = _popularGames

    private val _newReleases = MutableLiveData<List<Game>>()
    val newReleases: LiveData<List<Game>> = _newReleases

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPlatform = PlatformType.ALL

    init {
        loadGames()
    }

    fun setPlatformFilter(platformType: PlatformType) {
        currentPlatform = platformType
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val popularResult = repository.getPopularGames(currentPlatform)
            val newResult = repository.getNewReleases(currentPlatform)

            popularResult.onSuccess { _popularGames.value = it.results }
                .onFailure { _error.value = it.message }

            newResult.onSuccess { _newReleases.value = it.results }

            _isLoading.value = false
        }
    }
}
