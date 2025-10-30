package com.aimusicplayer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aimusicplayer.app.domain.model.Playlist

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val coverImageUrl: String?,
    val isPublic: Boolean = false,
    val createdAt: String?,
    val updatedAt: String?,
    val songCount: Int = 0
)

fun PlaylistEntity.toDomain(): Playlist {
    return Playlist(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverImageUrl = coverImageUrl,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        songCount = songCount
    )
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverImageUrl = coverImageUrl,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        songCount = songCount
    )
}
