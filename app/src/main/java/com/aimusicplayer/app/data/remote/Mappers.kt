package com.aimusicplayer.app.data.remote

import com.aimusicplayer.app.domain.model.*

fun UserResponse.toDomain(): User {
    return User(
        id = id,
        email = email,
        displayName = displayName,
        profileImageUrl = profileImageUrl,
        isAdmin = isAdmin,
        themePreference = themePreference
    )
}

fun SongDto.toDomain(): Song {
    val lyricsSync = lyricsSync?.let {
        LyricsSync(
            lines = it.lines.map { line ->
                LyricLine(
                    startTime = line.startTime,
                    endTime = line.endTime,
                    text = line.text,
                    words = line.words?.map { word ->
                        WordTiming(
                            word = word.word,
                            startTime = word.startTime,
                            endTime = word.endTime
                        )
                    } ?: emptyList()
                )
            }
        )
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

fun PlaylistDto.toDomain(): Playlist {
    return Playlist(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverImageUrl = coverImageUrl,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt,
        songCount = songCount ?: 0
    )
}

fun RecommendationDto.toDomain(): Recommendation {
    return Recommendation(
        song = song.toDomain(),
        reason = reason,
        score = score
    )
}
