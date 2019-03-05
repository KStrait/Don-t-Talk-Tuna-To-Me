package com.kylestrait.donttalktunatome.manager

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import javax.inject.Inject
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.STATE_READY
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import javax.inject.Singleton
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource



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

    fun setupPlayer(context: Context, item: Item?) {
        Log.d("setupPlayer", item?.enclosure?.url)

        mItem = item
        mContext = context

        isBuffering?.value = true


        if(exoPlayer == null) {
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
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying) {
                Log.d("audioManager", "11111")
                mediaPlayer.start()
                isPlaying?.value = true
            } else {
                Log.d("audioManager", "22222")
                mediaPlayer.pause()
                isPlaying?.value = false
            }
        } else {
            Log.d("audioManager", "NULL")
        }

        if(exoPlayer?.playWhenReady == true) {
            exoPlayer?.playWhenReady = false
        }else{
            exoPlayer?.playWhenReady = true
        }
    }

    fun getPlayer(): MediaPlayer {
        return mediaPlayer
    }

    fun getExoMediaPlayer(): ExoPlayer? {
        return exoPlayer
    }

    fun setPlayerViewStuff(playerView: PlayerControlView?) {
        playerView?.setPlayer(exoPlayer);

        exoPlayer?.setPlayWhenReady(playWhenReady)
        exoPlayer?.seekTo(currentWindow, playbackPosition)

        val mediaSource: MediaSource = buildMediaSource(Uri.parse(mItem?.enclosure?.url))

        exoPlayer?.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri?): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri)
    }

}
