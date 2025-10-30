package com.aimusicplayer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aimusicplayer.app.domain.model.PlaylistSong

@Entity(tableName = "playlist_songs")
data class PlaylistSongEntity(
    @PrimaryKey val id: String,
    val playlistId: String,
    val songId: String,
    val position: Int,
    val addedAt: String?
)

fun PlaylistSongEntity.toDomain(): PlaylistSong {
    return PlaylistSong(
        id = id,
        playlistId = playlistId,
        songId = songId,
        position = position,
        addedAt = addedAt
    )
}

fun PlaylistSong.toEntity(): PlaylistSongEntity {
    return PlaylistSongEntity(
        id = id,
        playlistId = playlistId,
        songId = songId,
        position = position,
        addedAt = addedAt
    )
}
