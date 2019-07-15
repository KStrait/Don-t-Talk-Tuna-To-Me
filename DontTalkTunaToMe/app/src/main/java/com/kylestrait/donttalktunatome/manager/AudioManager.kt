package com.kylestrait.donttalktunatome.manager

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import javax.inject.Inject
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Environment
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.STATE_READY
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import javax.inject.Singleton
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import java.io.File


@Singleton
class AudioManager @Inject constructor() {
    private val TAG: String? = AudioManager::class.simpleName

    val mediaPlayer = MediaPlayer()
    var mItem: Item? = null
    var mContext: Context? = null

    val isPlaying: MutableLiveData<Boolean>? = MutableLiveData()
    val isBuffering: MutableLiveData<Boolean>? = MutableLiveData()
    var duration: MutableLiveData<Int>? = MutableLiveData()
    var isReady: MutableLiveData<Boolean>? = MutableLiveData()

    var playWhenReady = true
    var currentWindow = 0
    var playbackPosition: Long = 0

    var exoPlayer: ExoPlayer? = null

    var isDownloaded: Boolean = false

    fun setupPlayer(context: Context, item: Item?, isDownloaded: Boolean) {
        Log.d("setupPlayer", item?.enclosure?.url)
        mItem = item
        mContext = context

        this.isDownloaded = isDownloaded

        isBuffering?.value = true

        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(mContext),
                DefaultTrackSelector(), DefaultLoadControl()
            )
        }
    }

    private fun setBuffering() {
        isBuffering?.value = true
        Log.d(TAG, "isLooping()")
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
            var mediaSource: MediaSource = buildLocalMediaSource(getUriForDownloadedEpisode(mItem?.title!!))
            exoPlayer?.prepare(mediaSource, true, false)
            Log.d(TAG, "playing download")
        } else {
            var mediaSource: MediaSource = buildMediaSource(Uri.parse(mItem?.enclosure?.url))
            exoPlayer?.prepare(mediaSource, true, false)
            Log.d(TAG, "playing stream")
        }
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri)
    }

    private fun buildLocalMediaSource(uri: Uri?): MediaSource {
        return ExtractorMediaSource.Factory(FileDataSourceFactory()).createMediaSource(uri)
    }

    fun getExoSession(): MediaSessionCompat {
        return MediaSessionCompat(mContext, TAG)
    }

    fun releasePlayer() {
        mediaPlayer.release()
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

        Log.d(TAG, Uri.fromFile(newFile).path)
        return Uri.fromFile(newFile)
    }
}
