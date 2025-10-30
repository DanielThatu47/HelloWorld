package com.aimusicplayer.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.aimusicplayer.app.R
import com.aimusicplayer.app.domain.model.Song
import com.aimusicplayer.app.presentation.MainActivity
import com.aimusicplayer.app.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : Service() {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private val binder = MusicBinder()
    private var currentSong: Song? = null
    private var queue: List<Song> = emptyList()
    private var currentIndex: Int = -1

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setupPlayer()
    }

    private fun setupPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> updateNotification()
                    Player.STATE_ENDED -> {
                        playNext()
                    }
                }
            }
        })
    }

    fun playSong(song: Song) {
        currentSong = song
        val mediaItem = MediaItem.fromUri(song.fileUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        updateNotification()
    }

    fun playQueue(songs: List<Song>, startIndex: Int = 0) {
        queue = songs
        currentIndex = startIndex
        if (startIndex < songs.size) {
            playSong(songs[startIndex])
        }
    }

    fun playNext() {
        if (currentIndex < queue.size - 1) {
            currentIndex++
            playSong(queue[currentIndex])
        }
    }

    fun playPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            playSong(queue[currentIndex])
        }
    }

    fun pause() {
        exoPlayer.pause()
        updateNotification()
    }

    fun resume() {
        exoPlayer.play()
        updateNotification()
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun getCurrentPosition(): Long = exoPlayer.currentPosition
    fun getDuration(): Long = exoPlayer.duration
    fun isPlaying(): Boolean = exoPlayer.isPlaying
    fun getCurrentSong(): Song? = currentSong

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(currentSong?.title ?: "No song")
            .setContentText(currentSong?.artist ?: "")
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()

        startForeground(Constants.NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}
