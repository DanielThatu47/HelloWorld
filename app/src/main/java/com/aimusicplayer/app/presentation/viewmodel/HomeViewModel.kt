package com.aimusicplayer.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            songRepository.getSongs()
                .onSuccess { songs ->
                    _songs.value = songs
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun searchSongs(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            songRepository.searchSongs(query)
                .onSuccess { songs ->
                    _songs.value = songs
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun refresh() {
        loadSongs()
    }
}
