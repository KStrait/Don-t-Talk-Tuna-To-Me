package com.kylestrait.donttalktunatome.util

import com.google.android.exoplayer2.Player
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.Nullable
import android.text.Html
import android.util.Log
import android.view.Window
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Constants
import com.kylestrait.donttalktunatome.manager.AudioManager
import com.kylestrait.donttalktunatome.manager.MediaService
import javax.inject.Inject
import kotlin.properties.Delegates


class DescriptionAdapter @Inject constructor(audioManager: AudioManager, mContext: Context) : PlayerNotificationManager.MediaDescriptionAdapter {

    var context: Context by Delegates.notNull()
    var audioManager: AudioManager by Delegates.notNull()

    init {
        context = mContext
        this.audioManager = audioManager
    }

    override fun getCurrentContentTitle(player: Player): String? {
        val window = player.currentWindowIndex
        return getTitle(window)
    }

    @Nullable
    override fun getCurrentContentText(player: Player): String? {
        val window = player.currentWindowIndex
        return getDescription(window)
    }

    @Nullable
    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val window = player.currentWindowIndex
        val largeIcon = getLargeIcon(window)
//        if (largeIcon == null && getLargeIconUri(window) != null) {
//            // load bitmap async
//            loadBitmap(getLargeIconUri(window), callback)
//            return getPlaceholderBitmap()
//        }
        return largeIcon
    }

    @Nullable
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val window = player.currentWindowIndex
        return createPendingIntent(window)
    }

    fun getTitle(window: Int): String? {
        return audioManager.mItem?.title
    }

    fun getDescription(window: Int): String? {
        return Html.fromHtml(audioManager.mItem?.description).toString()
    }

    fun getLargeIcon(window: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.tuna_icon)
    }

    fun createPendingIntent(window: Int): PendingIntent {
        var intent: Intent = Intent(context, MediaService::class.java)
        intent.action = Constants.ACTION.STARTFOREGROUND_ACTION

        Log.d("DescriptionAdapter", "createPendingIntent")

        return PendingIntent.getService(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}