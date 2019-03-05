package com.kylestrait.donttalktunatome.manager

import android.content.Intent
import android.os.IBinder
import android.Manifest.permission.FOREGROUND_SERVICE
import android.app.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.kylestrait.donttalktunatome.MainActivity
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.TunaApplication
import com.kylestrait.donttalktunatome.data.Constants
import com.kylestrait.donttalktunatome.di.AppModule
import com.kylestrait.donttalktunatome.di.DaggerAppComponent
import dagger.android.AndroidInjection
import javax.inject.Inject


class MediaService: Service() {
    private val LOG_TAG = "ForegroundService"
    private val TAG: String? = MediaService::class.simpleName

    @Inject
    lateinit var audioManager: AudioManager

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ")
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

            val icon = BitmapFactory.decodeResource(
                resources,
                R.drawable.play_icon
            )

            val notification = NotificationCompat.Builder(this@MediaService, "some_channel")
                .setContentTitle(audioManager.mItem?.title)
                .setTicker(audioManager.mItem?.description)
                .setContentText("Don't Talk Tuna To Me")
                .setSmallIcon(R.drawable.play_icon)
                .setLargeIcon(
                    Bitmap.createScaledBitmap(icon, 128, 128, false)
                )
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(
                    android.R.drawable.ic_media_play, "Play",
                    pplayIntent
                ).build()
            startForeground(
                101,
                notification
            )
        } else if (intent?.action.equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, "Clicked Previous")
        } else if (intent?.action.equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(LOG_TAG, "Clicked Play")
            audioManager.playPodcast()
        } else if (intent?.action.equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next")
        } else if (intent?.action.equals(
                Constants.ACTION.STOPFOREGROUND_ACTION
            )
        ) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent")
            stopForeground(true)
            stopSelf()
        }
        return Service.START_STICKY    }
}