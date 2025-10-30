package com.aimusicplayer.app.domain.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Int, // in milliseconds
    val fileUrl: String,
    val coverImageUrl: String?,
    val genre: String?,
    val year: Int?,
    val lyrics: String?,
    val lyricsSync: LyricsSync?,
    val playCount: Int = 0,
    val uploadDate: String?,
    val uploadedBy: String?,
    val fileSize: Long?,
    val bitrate: Int?,
    val format: String?
)
