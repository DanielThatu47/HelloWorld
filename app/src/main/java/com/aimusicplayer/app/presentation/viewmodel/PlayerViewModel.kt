package com.aimusicplayer.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimusicplayer.app.domain.model.PlayerState
import com.aimusicplayer.app.domain.model.RepeatMode
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.service.MusicPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState

    private var musicService: MusicPlayerService? = null

    fun setMusicService(service: MusicPlayerService) {
        musicService = service
        updatePlayerState()
    }

    fun playSong(song: Song) {
        musicService?.playSong(song)
        updatePlayerState()
    }

    fun playQueue(songs: List<Song>, startIndex: Int = 0) {
        musicService?.playQueue(songs, startIndex)
        _playerState.value = _playerState.value.copy(
            queue = songs,
            currentIndex = startIndex,
            currentSong = songs.getOrNull(startIndex)
        )
        updatePlayerState()
    }

    fun togglePlayPause() {
        if (_playerState.value.isPlaying) {
            musicService?.pause()
        } else {
            musicService?.resume()
        }
        updatePlayerState()
    }

    fun playNext() {
        musicService?.playNext()
        updatePlayerState()
    }

    fun playPrevious() {
        musicService?.playPrevious()
        updatePlayerState()
    }

    fun seekTo(position: Long) {
        musicService?.seekTo(position)
        updatePlayerState()
    }

    fun toggleShuffle() {
        _playerState.value = _playerState.value.copy(
            isShuffled = !_playerState.value.isShuffled
        )
    }

    fun toggleRepeat() {
        val newMode = when (_playerState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _playerState.value = _playerState.value.copy(repeatMode = newMode)
    }

    private fun updatePlayerState() {
        val service = musicService ?: return
        _playerState.value = _playerState.value.copy(
            currentSong = service.getCurrentSong(),
            isPlaying = service.isPlaying(),
            currentPosition = service.getCurrentPosition(),
            duration = service.getDuration()
        )
    }
}
