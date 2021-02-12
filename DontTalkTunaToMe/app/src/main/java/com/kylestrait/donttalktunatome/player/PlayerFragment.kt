package com.kylestrait.donttalktunatome.player

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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kylestrait.donttalktunatome.MainActivity
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit

class PlayerFragment @Inject constructor() : DaggerFragment() {
    var TAG: String? = PlayerFragment::class.simpleName

    var mBinding: FragmentPlayerBinding? = null
    var mEpisode: Item? = null

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mViewModel: PlayerViewModel? = null
    var mMainViewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mViewModel = ViewModelProviders.of(
            this,
            mViewModelFactory
        ).get(PlayerViewModel::class.java)

        mMainViewModel = (activity as MainActivity).provideMainViewModel()

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        mBinding?.executePendingBindings()

        mBinding?.viewModel = mViewModel
        mBinding?.mainViewModel = mMainViewModel

        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel?.posterLink?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d(TAG, it)
            Picasso.get().load("http://image.tmdb.org/t/p/w342/".plus(it)).into(mBinding?.posterImage)
        })

        mMainViewModel?.downloadProgress?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null && it > 0 && it < 100) {
                mBinding?.progress?.visibility = View.VISIBLE
                mBinding?.progress?.progress = it
            } else {
                mBinding?.progress?.visibility = View.GONE
            }
        })

        var episodeCall: LiveData<Item> = mMainViewModel?.episode!!
        val episodeObserver = androidx.lifecycle.Observer<Item> {
            mEpisode = it
            mViewModel?.getImdbFeed("", mViewModel!!.stripChars(mEpisode?.title!!.toString()))
            mBinding?.myPodcast = it
        }

        episodeCall.observe(viewLifecycleOwner, episodeObserver)

        var isDownloaded: LiveData<Boolean> = Transformations.switchMap(episodeCall) { mMainViewModel?.isDownloaded }
        isDownloaded.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                mBinding?.downloadBtn?.isSelected = true
            } else if (it == false) {
                mBinding?.downloadBtn?.isSelected = false
            }
        })
    }
}