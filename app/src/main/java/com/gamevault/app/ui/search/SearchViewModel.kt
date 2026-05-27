package com.gamevault.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.app.data.model.Game
import com.gamevault.app.data.model.PlatformType
import com.gamevault.app.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Game>>()
    val searchResults: LiveData<List<Game>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var searchJob: Job? = null
    private var currentPlatform = PlatformType.ALL

    fun setPlatformFilter(platformType: PlatformType) {
        currentPlatform = platformType
    }

    fun search(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // debounce
            _isLoading.value = true
            _error.value = null

            repository.searchGames(query, currentPlatform)
                .onSuccess { _searchResults.value = it.results }
                .onFailure { _error.value = it.message }

            _isLoading.value = false
        }
    }
}
