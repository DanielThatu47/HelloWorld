package com.aimusicplayer.app.data.local.dao

import androidx.room.*
import com.aimusicplayer.app.data.local.entity.SongEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM songs WHERE id IN (SELECT songId FROM favorites WHERE userId = :userId)")
    suspend fun getFavoriteSongs(userId: String): List<SongEntity>

    @Query("SELECT songId FROM favorites WHERE userId = :userId AND songId = :songId")
    suspend fun isFavorite(userId: String, songId: String): String?

    @Query("INSERT INTO favorites (userId, songId) VALUES (:userId, :songId)")
    suspend fun addFavorite(userId: String, songId: String)

    @Query("DELETE FROM favorites WHERE userId = :userId AND songId = :songId")
    suspend fun removeFavorite(userId: String, songId: String)
}
