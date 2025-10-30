package com.aimusicplayer.app.data.local.dao

import androidx.room.*
import com.aimusicplayer.app.data.local.entity.SongEntity

@Dao
interface ListeningHistoryDao {
    @Query("SELECT * FROM songs WHERE id IN (SELECT songId FROM listening_history WHERE userId = :userId ORDER BY playedAt DESC LIMIT :limit)")
    suspend fun getRecentlyPlayed(userId: String, limit: Int): List<SongEntity>

    @Query("SELECT * FROM songs WHERE id IN (SELECT songId FROM listening_history WHERE userId = :userId GROUP BY songId ORDER BY COUNT(*) DESC LIMIT :limit)")
    suspend fun getMostPlayed(userId: String, limit: Int): List<SongEntity>

    @Query("INSERT INTO listening_history (userId, songId, playedAt, playDuration, completed) VALUES (:userId, :songId, :playedAt, :playDuration, :completed)")
    suspend fun recordPlayback(userId: String, songId: String, playedAt: Long, playDuration: Int?, completed: Boolean)
}
