package com.gamevault.app.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gamevault.app.data.model.FavoriteGame
import com.gamevault.app.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    private val _favorites = MutableLiveData<List<FavoriteGame>>()
    val favorites: LiveData<List<FavoriteGame>> = _favorites

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _favorites.value = repository.getAll()
        }
    }
}
