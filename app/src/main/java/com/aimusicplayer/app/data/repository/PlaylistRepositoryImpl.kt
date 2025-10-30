package com.aimusicplayer.app.data.repository

import com.aimusicplayer.app.data.local.dao.PlaylistDao
import com.aimusicplayer.app.data.local.dao.PlaylistSongDao
import com.aimusicplayer.app.data.remote.ApiService
import com.aimusicplayer.app.domain.model.Playlist
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val apiService: ApiService,
    private val playlistDao: PlaylistDao,
    private val playlistSongDao: PlaylistSongDao
) : PlaylistRepository {

    override suspend fun getUserPlaylists(userId: String): Result<List<Playlist>> {
        return try {
            val response = apiService.getUserPlaylists("Bearer token", userId) // TODO: Get actual token
            Result.success(response.playlists.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistById(id: String): Result<Playlist> {
        return try {
            val response = apiService.getPlaylistById(id)
            Result.success(response.playlist.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPlaylist(
        userId: String,
        name: String,
        description: String?
    ): Result<Playlist> {
        return try {
            val response = apiService.createPlaylist(
                "Bearer token", // TODO: Get actual token
                com.aimusicplayer.app.data.remote.CreatePlaylistRequest(name, description)
            )
            Result.success(response.playlist.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist): Result<Playlist> {
        return try {
            val response = apiService.updatePlaylist(
                "Bearer token", // TODO: Get actual token
                playlist.id,
                com.aimusicplayer.app.data.remote.UpdatePlaylistRequest(
                    playlist.name,
                    playlist.description
                )
            )
            Result.success(response.playlist.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePlaylist(id: String): Result<Unit> {
        return try {
            apiService.deletePlaylist("Bearer token", id) // TODO: Get actual token
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addSongToPlaylist(playlistId: String, songId: String): Result<Unit> {
        return try {
            apiService.addSongToPlaylist(
                "Bearer token", // TODO: Get actual token
                playlistId,
                com.aimusicplayer.app.data.remote.AddSongRequest(songId, null)
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeSongFromPlaylist(playlistId: String, songId: String): Result<Unit> {
        return try {
            apiService.removeSongFromPlaylist("Bearer token", playlistId, songId) // TODO: Get actual token
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistSongs(playlistId: String): Result<List<Song>> {
        return try {
            val response = apiService.getPlaylistSongs(playlistId)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reorderPlaylistSongs(
        playlistId: String,
        songIds: List<String>
    ): Result<Unit> {
        // TODO: Implement reordering logic
        return Result.success(Unit)
    }

    override suspend fun getLocalPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylistsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
