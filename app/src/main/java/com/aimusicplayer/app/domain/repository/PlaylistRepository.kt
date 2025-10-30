package com.aimusicplayer.app.domain.repository

import com.aimusicplayer.app.domain.model.Playlist
import com.aimusicplayer.app.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getUserPlaylists(userId: String): Result<List<Playlist>>
    suspend fun getPlaylistById(id: String): Result<Playlist>
    suspend fun createPlaylist(userId: String, name: String, description: String?): Result<Playlist>
    suspend fun updatePlaylist(playlist: Playlist): Result<Playlist>
    suspend fun deletePlaylist(id: String): Result<Unit>
    suspend fun addSongToPlaylist(playlistId: String, songId: String): Result<Unit>
    suspend fun removeSongFromPlaylist(playlistId: String, songId: String): Result<Unit>
    suspend fun getPlaylistSongs(playlistId: String): Result<List<Song>>
    suspend fun reorderPlaylistSongs(playlistId: String, songIds: List<String>): Result<Unit>
    suspend fun getLocalPlaylists(): Flow<List<Playlist>>
}
