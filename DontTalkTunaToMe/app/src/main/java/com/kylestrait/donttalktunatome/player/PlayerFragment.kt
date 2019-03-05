package com.kylestrait.donttalktunatome.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.util.StringUtil
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.FragmentPlayerBinding
import com.kylestrait.donttalktunatome.manager.AudioManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import com.kylestrait.donttalktunatome.BR
import com.kylestrait.donttalktunatome.data.Constants
import com.kylestrait.donttalktunatome.manager.MediaService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerControlView
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit

class PlayerFragment @Inject constructor() : DaggerFragment() {
    var TAG: String? = PlayerFragment::class.simpleName

    var mBinding: FragmentPlayerBinding? = null
    var mEpisode: Item? = null
    var exoPlayer: ExoPlayer? = null

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mAudioManager: AudioManager


    var mViewModel: PlayerViewModel? = null
    var mMainViewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mViewModel = ViewModelProviders.of(
            this,
            mViewModelFactory
        ).get(PlayerViewModel::class.java)

        mMainViewModel = ViewModelProviders.of(
            activity!!,
            mViewModelFactory
        ).get(MainViewModel::class.java)

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        mBinding?.executePendingBindings()

        mBinding?.viewModel = mViewModel

        mBinding?.mainViewModel = mMainViewModel

        mMainViewModel?.episode?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mEpisode = it
                Log.d(TAG, it.title.plus(" : TITLE"))
                mViewModel?.setAudioManager(activity!!, mEpisode)
                mBinding?.myPodcast = it

                exoPlayer = mAudioManager.getExoMediaPlayer()
                val playerView = mBinding?.playerView
                mAudioManager.setPlayerViewStuff(playerView)
                playerView?.showTimeoutMs = 0

                if (mEpisode?.title?.contains("(")!!) {
                    mViewModel?.getImdbFeed("", stripChars(mEpisode?.title!!))
                }
            }
        })

        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mViewModel?.posterLink?.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it)
            Picasso.get().load("http://image.tmdb.org/t/p/w342/".plus(it)).into(mBinding?.posterImage)
        })
    }

    fun stripChars(title: String): String {
        return title.substring(title.indexOf("(") + 1, title.indexOf(")"))
    }

    override fun onResume() {
        super.onResume()

        val startIntent = Intent(activity, MediaService::class.java)
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION)
        activity?.startService(startIntent)
    }
}