package com.aimusicplayer.app.data.remote

import com.aimusicplayer.app.domain.model.*
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): AuthResponse

    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): UserResponse

    // Songs
    @GET("songs")
    suspend fun getSongs(): SongsResponse

    @GET("songs/{id}")
    suspend fun getSongById(@Path("id") id: String): SongResponse

    @GET("songs/search")
    suspend fun searchSongs(@Query("q") query: String): SongsResponse

    @GET("songs/genre/{genre}")
    suspend fun getSongsByGenre(@Path("genre") genre: String): SongsResponse

    @GET("songs/artist/{artist}")
    suspend fun getSongsByArtist(@Path("artist") artist: String): SongsResponse

    @GET("songs/recommendations")
    suspend fun getRecommendations(@Header("Authorization") token: String): RecommendationsResponse

    @GET("users/{userId}/history/recent")
    suspend fun getRecentlyPlayed(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 50
    ): SongsResponse

    @GET("users/{userId}/history/most-played")
    suspend fun getMostPlayed(
        @Path("userId") userId: String,
        @Query("limit") limit: Int = 50
    ): SongsResponse

    // Playlists
    @GET("playlists")
    suspend fun getUserPlaylists(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): PlaylistsResponse

    @GET("playlists/{id}")
    suspend fun getPlaylistById(@Path("id") id: String): PlaylistResponse

    @POST("playlists")
    suspend fun createPlaylist(
        @Header("Authorization") token: String,
        @Body request: CreatePlaylistRequest
    ): PlaylistResponse

    @PUT("playlists/{id}")
    suspend fun updatePlaylist(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: UpdatePlaylistRequest
    ): PlaylistResponse

    @DELETE("playlists/{id}")
    suspend fun deletePlaylist(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Unit

    @POST("playlists/{id}/songs")
    suspend fun addSongToPlaylist(
        @Header("Authorization") token: String,
        @Path("id") playlistId: String,
        @Body request: AddSongRequest
    ): Unit

    @DELETE("playlists/{id}/songs/{songId}")
    suspend fun removeSongFromPlaylist(
        @Header("Authorization") token: String,
        @Path("id") playlistId: String,
        @Path("songId") songId: String
    ): Unit

    @GET("playlists/{id}/songs")
    suspend fun getPlaylistSongs(@Path("id") id: String): SongsResponse

    // Favorites
    @POST("favorites")
    suspend fun addToFavorites(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    ): Unit

    @DELETE("favorites/{songId}")
    suspend fun removeFromFavorites(
        @Header("Authorization") token: String,
        @Path("songId") songId: String
    ): Unit

    @GET("favorites")
    suspend fun getFavorites(@Header("Authorization") token: String): SongsResponse

    // Listening History
    @POST("history")
    suspend fun recordPlayback(
        @Header("Authorization") token: String,
        @Body request: PlaybackRequest
    ): Unit

    // Admin
    @POST("admin/songs/upload")
    suspend fun uploadSong(
        @Header("Authorization") token: String,
        @Body request: UploadSongRequest
    ): SongResponse

    @GET("admin/analytics")
    suspend fun getAnalytics(@Header("Authorization") token: String): AnalyticsResponse
}

// Request/Response DTOs
data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val email: String, val password: String, val displayName: String?)
data class AuthResponse(val token: String, val user: UserResponse)
data class UserResponse(
    val id: String,
    val email: String,
    val displayName: String?,
    val profileImageUrl: String?,
    val isAdmin: Boolean,
    val themePreference: String
)

data class SongsResponse(val songs: List<SongDto>)
data class SongResponse(val song: SongDto)
data class SongDto(
    val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Int,
    val fileUrl: String,
    val coverImageUrl: String?,
    val genre: String?,
    val year: Int?,
    val lyrics: String?,
    val lyricsSync: LyricsSyncDto?,
    val playCount: Int,
    val uploadDate: String?,
    val uploadedBy: String?,
    val fileSize: Long?,
    val bitrate: Int?,
    val format: String?
)

data class LyricsSyncDto(val lines: List<LyricLineDto>)
data class LyricLineDto(
    val startTime: Long,
    val endTime: Long,
    val text: String,
    val words: List<WordTimingDto>?
)
data class WordTimingDto(val word: String, val startTime: Long, val endTime: Long)

data class PlaylistsResponse(val playlists: List<PlaylistDto>)
data class PlaylistResponse(val playlist: PlaylistDto)
data class PlaylistDto(
    val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val coverImageUrl: String?,
    val isPublic: Boolean,
    val createdAt: String?,
    val updatedAt: String?,
    val songCount: Int?
)

data class CreatePlaylistRequest(val name: String, val description: String?)
data class UpdatePlaylistRequest(val name: String?, val description: String?)
data class AddSongRequest(val songId: String, val position: Int?)
data class FavoriteRequest(val songId: String)
data class PlaybackRequest(val songId: String, val playDuration: Int?, val completed: Boolean)
data class UploadSongRequest(
    val title: String,
    val artist: String,
    val album: String?,
    val genre: String?,
    val year: Int?,
    val fileUrl: String,
    val coverImageUrl: String?
)

data class RecommendationsResponse(val recommendations: List<RecommendationDto>)
data class RecommendationDto(val song: SongDto, val reason: String, val score: Float)
data class AnalyticsResponse(val totalSongs: Int, val totalUsers: Int, val totalPlays: Long)
