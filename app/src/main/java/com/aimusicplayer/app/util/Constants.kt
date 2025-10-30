package com.aimusicplayer.app.util

object Constants {
    const val BASE_URL = "https://your-api-url.com/api/"
    
    // SharedPreferences Keys
    const val PREFS_NAME = "ai_music_player_prefs"
    const val KEY_AUTH_TOKEN = "auth_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_THEME = "theme"
    const val KEY_DYNAMIC_COLORS = "dynamic_colors_enabled"
    
    // Database
    const val DATABASE_NAME = "music_database"
    
    // Media Session
    const val MEDIA_SESSION_TAG = "MusicPlayerService"
    
    // Notification
    const val NOTIFICATION_CHANNEL_ID = "music_player_channel"
    const val NOTIFICATION_ID = 1
    
    // Player
    const val CROSSFADE_DURATION_MS = 3000L
    const val DEFAULT_SLEEP_TIMER_MINUTES = 30
}
