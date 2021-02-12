package com.kylestrait.donttalktunatome.manager

import android.content.Context
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import javax.inject.Inject
import android.net.Uri
import android.os.Environment
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import javax.inject.Singleton
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.FileDataSource
import java.io.File


@Singleton
class AudioManager @Inject constructor() {
    private val TAG: String? = AudioManager::class.simpleName

    var mItem: Item? = null
    var mContext: Context? = null

    private val isBuffering: MutableLiveData<Boolean>? = MutableLiveData()
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    var exoPlayer: ExoPlayer? = null

    private var isDownloaded: Boolean = false

    fun setupPlayer(context: Context, item: Item?, isDownloaded: Boolean) {
        Log.d("setupPlayer", item?.enclosure?.url!!)
        mItem = item
        mContext = context

        this.isDownloaded = isDownloaded

        isBuffering?.value = true

        if (exoPlayer == null) {
            mContext?.let {
                exoPlayer = SimpleExoPlayer.Builder(it).build()
            }
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(
//                DefaultRenderersFactory(mContext),
//                DefaultTrackSelector(), DefaultLoadControl()
//            )
        }
    }

    fun playPodcast() {
        exoPlayer?.playWhenReady = exoPlayer?.playWhenReady != true
    }

    fun getExoMediaPlayer(): ExoPlayer? {
        return exoPlayer
    }

    fun setPlayerViewStuff(playerView: PlayerControlView?) {
        playerView?.player = exoPlayer

        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.seekTo(currentWindow, playbackPosition)

        if (isDownloaded) {
            var mediaSource: MediaSource = buildLocalMediaSource(getUriForDownloadedEpisode(mItem?.title!!.toString()))
            exoPlayer?.prepare(mediaSource, true, false)
            Log.d(TAG, "playing download")
        } else {
            var mediaSource: MediaSource = buildMediaSource(Uri.parse(mItem?.enclosure?.url))
            exoPlayer?.prepare(mediaSource, true, false)
            Log.d(TAG, "playing stream")
        }
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }

    private fun buildLocalMediaSource(uri: Uri?): MediaSource {
        return ProgressiveMediaSource.Factory(FileDataSource.Factory()).createMediaSource(uri)
    }

    fun getExoSession(): MediaSessionCompat {
        return MediaSessionCompat(mContext!!, TAG!!)
    }

    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun shutDown() {
        if (exoPlayer != null) {
            exoPlayer?.playWhenReady = false
            exoPlayer?.stop()
            exoPlayer?.seekTo(0)
        }
    }

    fun getUriForDownloadedEpisode(name: String): Uri {
        var newTitle: String? = name.replace("\\s".toRegex(), "")
        var newFile =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), newTitle.plus(".mp3"))

        Log.d(TAG, Uri.fromFile(newFile).path!!)
        return Uri.fromFile(newFile)
    }
}
