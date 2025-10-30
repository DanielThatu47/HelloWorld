package com.aimusicplayer.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.aimusicplayer.app.data.remote.ApiService
import com.aimusicplayer.app.data.remote.toDomain
import com.aimusicplayer.app.domain.model.User
import com.aimusicplayer.app.domain.repository.AuthRepository
import com.aimusicplayer.app.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : AuthRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _isLoggedIn = MutableStateFlow(getAuthToken() != null)
    override fun isLoggedIn(): Flow<Boolean> = _isLoggedIn

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = apiService.login(
                com.aimusicplayer.app.data.remote.LoginRequest(email, password)
            )
            saveAuthToken(response.token)
            _isLoggedIn.value = true
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(
        email: String,
        password: String,
        displayName: String?
    ): Result<String> {
        return try {
            val response = apiService.signup(
                com.aimusicplayer.app.data.remote.SignupRequest(email, password, displayName)
            )
            saveAuthToken(response.token)
            _isLoggedIn.value = true
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        prefs.edit().remove(Constants.KEY_AUTH_TOKEN).apply()
        prefs.edit().remove(Constants.KEY_USER_ID).apply()
        _isLoggedIn.value = false
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            val token = getAuthToken() ?: return null
            val response = apiService.getCurrentUser("Bearer $token")
            response.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun refreshToken(): Result<String> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("No token"))
            val response = apiService.refreshToken("Bearer $token")
            saveAuthToken(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveAuthToken(token: String) {
        prefs.edit().putString(Constants.KEY_AUTH_TOKEN, token).apply()
    }

    override suspend fun getAuthToken(): String? {
        return prefs.getString(Constants.KEY_AUTH_TOKEN, null)
    }
}
