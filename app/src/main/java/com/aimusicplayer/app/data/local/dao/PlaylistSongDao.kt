package com.aimusicplayer.app.data.local.dao

import androidx.room.*
import com.aimusicplayer.app.data.local.entity.PlaylistSongEntity

@Dao
interface PlaylistSongDao {
    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId ORDER BY position ASC")
    suspend fun getSongsByPlaylistId(playlistId: String): List<PlaylistSongEntity>

    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun getPlaylistSong(playlistId: String, songId: String): PlaylistSongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSong(playlistSong: PlaylistSongEntity)

    @Delete
    suspend fun deletePlaylistSong(playlistSong: PlaylistSongEntity)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun deletePlaylistSongByIds(playlistId: String, songId: String)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun deleteAllPlaylistSongs(playlistId: String)

    @Update
    suspend fun updatePlaylistSong(playlistSong: PlaylistSongEntity)
}
