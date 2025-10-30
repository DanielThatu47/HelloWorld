package com.aimusicplayer.app.domain.model

data class Playlist(
    val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val coverImageUrl: String?,
    val isPublic: Boolean = false,
    val createdAt: String?,
    val updatedAt: String?,
    val songCount: Int = 0
)
