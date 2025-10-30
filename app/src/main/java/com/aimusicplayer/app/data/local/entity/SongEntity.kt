package com.aimusicplayer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.domain.model.LyricsSync
import com.google.gson.Gson

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Int,
    val fileUrl: String,
    val coverImageUrl: String?,
    val genre: String?,
    val year: Int?,
    val lyrics: String?,
    val lyricsSyncJson: String?,
    val playCount: Int = 0,
    val uploadDate: String?,
    val uploadedBy: String?,
    val fileSize: Long?,
    val bitrate: Int?,
    val format: String?
)

fun SongEntity.toDomain(): Song {
    val lyricsSync = lyricsSyncJson?.let {
        try {
            Gson().fromJson(it, LyricsSync::class.java)
        } catch (e: Exception) {
            null
        }
    }
    return Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        fileUrl = fileUrl,
        coverImageUrl = coverImageUrl,
        genre = genre,
        year = year,
        lyrics = lyrics,
        lyricsSync = lyricsSync,
        playCount = playCount,
        uploadDate = uploadDate,
        uploadedBy = uploadedBy,
        fileSize = fileSize,
        bitrate = bitrate,
        format = format
    )
}

fun Song.toEntity(): SongEntity {
    val lyricsSyncJson = lyricsSync?.let {
        try {
            Gson().toJson(it)
        } catch (e: Exception) {
            null
        }
    }
    return SongEntity(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        fileUrl = fileUrl,
        coverImageUrl = coverImageUrl,
        genre = genre,
        year = year,
        lyrics = lyrics,
        lyricsSyncJson = lyricsSyncJson,
        playCount = playCount,
        uploadDate = uploadDate,
        uploadedBy = uploadedBy,
        fileSize = fileSize,
        bitrate = bitrate,
        format = format
    )
}
