package com.kylestrait.donttalktunatome.manager

import android.content.Intent
import android.os.IBinder
import android.app.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kylestrait.donttalktunatome.MainActivity
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Constants
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaService @Inject constructor(): Service() {
    private val TAG: String? = MediaService::class.simpleName

    @Inject
    lateinit var audioManager: AudioManager

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")

        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)

        Log.d(TAG, "onCreate")

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(TAG, "Received Start Foreground Intent ")
            val notificationIntent = Intent(this, MainActivity::class.java)
            notificationIntent.action = Constants.ACTION.MAIN_ACTION
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                this, 0,
                notificationIntent, 0
            )

            val playIntent = Intent(this, MediaService::class.java)
            playIntent.action = Constants.ACTION.PLAY_ACTION
            val pplayIntent = PendingIntent.getService(
                this, 0,
                playIntent, 0
            )

            val stopIntent = Intent(this, MediaService::class.java)
            stopIntent.action = Constants.ACTION.STOPFOREGROUND_ACTION
            val pstopintent = PendingIntent.getService(
                this, 0,
                stopIntent, 0
            )

            val icon = BitmapFactory.decodeResource(
                resources,
                R.drawable.tuna_icon
            )

            val notification = NotificationCompat.Builder(this@MediaService, "some_channel")
                .setContentTitle(audioManager.mItem?.title)
                .setTicker(audioManager.mItem?.description)
//                .setContentText("Don't Talk Tuna To Me")
                .setSmallIcon(R.drawable.play_icon)
                .setLargeIcon(
                    Bitmap.createScaledBitmap(icon, 128, 128, false)
                )
                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_media_play, "Play", pplayIntent)
                .addAction(R.drawable.ic_stop, "Stop", pstopintent)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(audioManager.getExoSession().sessionToken))
                .build()
            startForeground(
                101,
                notification
            )
        } else if (intent?.action.equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(TAG, "Clicked Previous")
        } else if (intent?.action.equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(TAG, "Clicked Play")
            audioManager.playPodcast()
        } else if (intent?.action.equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(TAG, "Clicked Next")
        } else if (intent?.action.equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(TAG, "Received Stop Foreground Intent")
//            audioManager.shutDown()
            stopForeground(false)
//            stopSelf()
        }

        return Service.START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(TAG, "onTaskRemoved")
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "onTaskRemoved")
        audioManager.shutDown()
        stopSelf()
        stopForeground(true)
    }

    fun closeService(){
//        audioManager.shutDown()
//        stopForeground(true)
//        stopSelf()
    }
}