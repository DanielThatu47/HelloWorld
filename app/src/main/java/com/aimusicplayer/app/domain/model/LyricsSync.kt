package com.aimusicplayer.app.domain.model

data class LyricsSync(
    val lines: List<LyricLine>
)

data class LyricLine(
    val startTime: Long, // milliseconds
    val endTime: Long,
    val text: String,
    val words: List<WordTiming> = emptyList()
)

data class WordTiming(
    val word: String,
    val startTime: Long,
    val endTime: Long
)
