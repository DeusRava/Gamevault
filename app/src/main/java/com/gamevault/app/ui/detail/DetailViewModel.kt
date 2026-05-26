package com.gamevault.app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.app.data.model.Game
import com.gamevault.app.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadGame(gameId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getGameDetail(gameId)
                .onSuccess { _game.value = it }
                .onFailure { _error.value = it.message ?: "Failed to load game" }
            _isLoading.value = false
        }
    }
}
