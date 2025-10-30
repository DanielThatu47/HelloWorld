package com.aimusicplayer.app.data.local.dao

import androidx.room.*
import com.aimusicplayer.app.data.local.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    suspend fun getPlaylistsByUserId(userId: String): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE userId = :userId")
    fun getPlaylistsByUserIdFlow(userId: String): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsFlow(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: String): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylistById(id: String)
}
