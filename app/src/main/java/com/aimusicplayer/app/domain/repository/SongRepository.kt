package com.aimusicplayer.app.domain.repository

import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.domain.model.Recommendation
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun getSongs(): Result<List<Song>>
    suspend fun getSongById(id: String): Result<Song>
    suspend fun searchSongs(query: String): Result<List<Song>>
    suspend fun getSongsByGenre(genre: String): Result<List<Song>>
    suspend fun getSongsByArtist(artist: String): Result<List<Song>>
    suspend fun getRecommendations(userId: String): Result<List<Recommendation>>
    suspend fun getRecentlyPlayed(userId: String, limit: Int = 50): Result<List<Song>>
    suspend fun getMostPlayed(userId: String, limit: Int = 50): Result<List<Song>>
    suspend fun getLocalSongs(): Flow<List<Song>>
    suspend fun cacheSong(song: Song)
}
