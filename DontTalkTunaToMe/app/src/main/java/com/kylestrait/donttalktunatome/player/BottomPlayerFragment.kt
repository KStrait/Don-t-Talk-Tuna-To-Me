package com.kylestrait.donttalktunatome.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.kylestrait.donttalktunatome.MainActivity
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Constants
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.FragmentBottomPlayerBinding
import com.kylestrait.donttalktunatome.manager.AudioManager
import com.kylestrait.donttalktunatome.manager.MediaService
import javax.inject.Inject
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.kylestrait.donttalktunatome.util.DescriptionAdapter
import com.kylestrait.donttalktunatome.widget.BaseFragment
import dagger.android.support.DaggerFragment

class BottomPlayerFragment @Inject constructor(): BaseFragment<PlayerViewModel>(PlayerViewModel::class.java) {
    var mMainViewModel: MainViewModel? = null
    var mViewModel: PlayerViewModel? = null
    var mBinding: FragmentBottomPlayerBinding? = null

    var mEpisode: Item? = null

    var exoPlayer: ExoPlayer? = null

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAudioManager: AudioManager

    lateinit var playerNotificationManager: PlayerNotificationManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mViewModel = ViewModelProviders.of(
            this,
            mViewModelFactory
        ).get(PlayerViewModel::class.java)

        mMainViewModel = (activity as MainActivity).provideMainViewModel()


        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_player, container, false)
        mBinding?.executePendingBindings()


        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        playerNotificationManager = PlayerNotificationManager(
            context,
            "some_channel",
            101,
            DescriptionAdapter(mAudioManager, context!!)
        )

        playerNotificationManager.setOngoing(false)

//        mMainViewModel?.episode?.observe(viewLifecycleOwner, Observer {
//            mEpisode = it
//            setupExoPlayerOther(it!!)
//        })
    }

    private fun setupExoPlayerOther(item: Item) {
        mEpisode = item
        mBinding?.item = mEpisode

        mViewModel?.setAudioManager(activity!!, mEpisode)
        exoPlayer = mAudioManager.getExoMediaPlayer()
        val playerView = mBinding?.playerView
        mAudioManager.setPlayerViewStuff(playerView)
        playerView?.showTimeoutMs = 0

        playerNotificationManager.setPlayer(exoPlayer)
        playerNotificationManager.setMediaSessionToken(mAudioManager.getExoSession().sessionToken)

//        startServiceThing()
    }

    override fun onResume() {
        super.onResume()

        mMainViewModel?.episode?.observe(viewLifecycleOwner, Observer {
            mEpisode = it
            setupExoPlayerOther(it!!)
        })
    }

//    fun isPlaying(): Boolean {
//        return exoPlayer?.getPlaybackState() == Player.STATE_READY && exoPlayer?.getPlayWhenReady()!!
//    }

    private fun startServiceThing() {
        val startIntent = Intent(activity, MediaService::class.java)
        startIntent.action = Constants.ACTION.STARTFOREGROUND_ACTION
        activity?.startService(startIntent)
    }

    override fun onPause() {
        super.onPause()
        mAudioManager.releasePlayer()
    }
}