package com.kylestrait.donttalktunatome.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
    var TAG: String = BottomPlayerFragment::class.java.simpleName

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
            context!!,
            "some_channel",
            101,
            DescriptionAdapter(mAudioManager, context!!)
        )

//        playerNotificationManager.setOngoing(false)
    }

    private fun setupExoPlayerOther(item: Item) {
        mEpisode = item
        mBinding?.item = mEpisode

        activity?.let {
            mViewModel?.setAudioManager(it, mEpisode)
            exoPlayer = mAudioManager.getExoMediaPlayer()
            val playerView = mBinding?.playerView
            mAudioManager.setPlayerViewStuff(playerView)
            playerView?.showTimeoutMs = 0

            playerNotificationManager.setPlayer(exoPlayer)
            playerNotificationManager.setMediaSessionToken(mAudioManager.getExoSession().sessionToken)
        }
    }

    override fun onResume() {
        super.onResume()

        mMainViewModel?.episode?.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "here 1")
            mEpisode = it
            setupExoPlayerOther(it!!)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager.setPlayer(null)
        mAudioManager.releasePlayer()
    }
}