package com.aimusicplayer.app.di

import android.content.Context
import androidx.room.Room
import com.aimusicplayer.app.data.local.MusicDatabase
import com.aimusicplayer.app.data.local.dao.*
import com.aimusicplayer.app.data.remote.ApiService
import com.aimusicplayer.app.data.repository.*
import com.aimusicplayer.app.domain.repository.*
import com.aimusicplayer.app.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.media3.exoplayer.ExoPlayer
import android.content.Context
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            "music_database"
        ).build()
    }

    @Provides
    fun provideSongDao(database: MusicDatabase): SongDao = database.songDao()

    @Provides
    fun providePlaylistDao(database: MusicDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun providePlaylistSongDao(database: MusicDatabase): PlaylistSongDao = database.playlistSongDao()

    @Provides
    fun provideFavoriteDao(database: MusicDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideListeningHistoryDao(database: MusicDatabase): ListeningHistoryDao = database.listeningHistoryDao()

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, context)
    }

    @Provides
    @Singleton
    fun provideSongRepository(
        apiService: ApiService,
        songDao: SongDao
    ): SongRepository {
        return SongRepositoryImpl(apiService, songDao)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(
        apiService: ApiService,
        playlistDao: PlaylistDao,
        playlistSongDao: PlaylistSongDao
    ): PlaylistRepository {
        return PlaylistRepositoryImpl(apiService, playlistDao, playlistSongDao)
    }

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}
