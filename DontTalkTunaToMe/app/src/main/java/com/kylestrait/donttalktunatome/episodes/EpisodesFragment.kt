package com.kylestrait.donttalktunatome.episodes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylestrait.donttalktunatome.R
import android.databinding.DataBindingUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.FragmentEpisodesBinding
import com.kylestrait.donttalktunatome.manager.AudioManager
import com.google.android.exoplayer2.ExoPlayer
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EpisodesFragment @Inject constructor() : DaggerFragment() {
    var TAG: String? = EpisodesFragment::class.simpleName

    var mBinding: FragmentEpisodesBinding? = null
    var mRV: RecyclerView? = null
    var mAdapter: EpisodesRecyclerViewAdapter? = null

    var mItems: MutableList<Item> = ArrayList<Item>()

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var exoPlayer: ExoPlayer? = null

    @Inject
    lateinit var mAudioManager: AudioManager

    var mViewModel: EpisodesViewModel? = null
    var mMainViewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_episodes, container, false)
        mBinding?.executePendingBindings()


        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = ViewModelProviders.of(
            this,
            mViewModelFactory
        ).get(EpisodesViewModel::class.java)

        mMainViewModel = ViewModelProviders.of(
            activity!!,
            mViewModelFactory
        ).get(MainViewModel::class.java)

        if (mBinding?.rv != null) {
            mRV = mBinding?.rv
            val context: Context? = mRV?.context

            mRV?.setHasFixedSize(true)

            mAdapter = EpisodesRecyclerViewAdapter(mMainViewModel)

            mRV?.adapter = mAdapter

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(
                mRV?.context,
                layoutManager.orientation
            )
            mRV?.addItemDecoration(dividerItemDecoration)
            mRV?.layoutManager = layoutManager

        }

        mMainViewModel?.mainFeed?.observe(viewLifecycleOwner, Observer {
            mItems.addAll(it?.body()?.channel?.item!!)

            Log.d(TAG, mItems.get(0).description)
            mAdapter?.setItems(mItems)
            mAdapter?.notifyDataSetChanged()
        })

    }

    override fun onStop() {
        super.onStop()

        mItems.clear()
    }
}