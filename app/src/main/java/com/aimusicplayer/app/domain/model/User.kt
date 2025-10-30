package com.aimusicplayer.app.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String?,
    val profileImageUrl: String?,
    val isAdmin: Boolean = false,
    val themePreference: String = "dark"
)
