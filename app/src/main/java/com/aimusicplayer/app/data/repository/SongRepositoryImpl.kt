package com.aimusicplayer.app.data.repository

import android.content.Context
import com.aimusicplayer.app.data.local.dao.SongDao
import com.aimusicplayer.app.data.local.entity.SongEntity
import com.aimusicplayer.app.data.remote.ApiService
import com.aimusicplayer.app.data.remote.toDomain
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.domain.model.Recommendation
import com.aimusicplayer.app.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongRepositoryImpl(
    private val apiService: ApiService,
    private val songDao: SongDao
) : SongRepository {

    override suspend fun getSongs(): Result<List<Song>> {
        return try {
            val response = apiService.getSongs()
            val songs = response.songs.map { it.toDomain() }
            // Cache songs locally
            songs.forEach { song ->
                songDao.insertSong(song.toEntity())
            }
            Result.success(songs)
        } catch (e: Exception) {
            // Try to get from local cache
            try {
                val localSongs = songDao.getAllSongs().map { it.toDomain() }
                Result.success(localSongs)
            } catch (localException: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getSongById(id: String): Result<Song> {
        return try {
            val response = apiService.getSongById(id)
            val song = response.song.toDomain()
            songDao.insertSong(song.toEntity())
            Result.success(song)
        } catch (e: Exception) {
            try {
                val localSong = songDao.getSongById(id)?.toDomain()
                if (localSong != null) {
                    Result.success(localSong)
                } else {
                    Result.failure(e)
                }
            } catch (localException: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun searchSongs(query: String): Result<List<Song>> {
        return try {
            val response = apiService.searchSongs(query)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSongsByGenre(genre: String): Result<List<Song>> {
        return try {
            val response = apiService.getSongsByGenre(genre)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSongsByArtist(artist: String): Result<List<Song>> {
        return try {
            val response = apiService.getSongsByArtist(artist)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecommendations(userId: String): Result<List<Recommendation>> {
        return try {
            val response = apiService.getRecommendations("Bearer token") // TODO: Get actual token
            Result.success(response.recommendations.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentlyPlayed(userId: String, limit: Int): Result<List<Song>> {
        return try {
            val response = apiService.getRecentlyPlayed(userId, limit)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMostPlayed(userId: String, limit: Int): Result<List<Song>> {
        return try {
            val response = apiService.getMostPlayed(userId, limit)
            Result.success(response.songs.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLocalSongs(): Flow<List<Song>> {
        return songDao.getAllSongsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun cacheSong(song: Song) {
        songDao.insertSong(song.toEntity())
    }
}
