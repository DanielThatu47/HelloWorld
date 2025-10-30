package com.aimusicplayer.app.domain.repository

import com.aimusicplayer.app.domain.model.User
import com.aimusicplayer.app.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String> // Returns auth token
    suspend fun signup(email: String, password: String, displayName: String?): Result<String>
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    suspend fun refreshToken(): Result<String>
    fun isLoggedIn(): Flow<Boolean>
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
}
