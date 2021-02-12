package com.kylestrait.donttalktunatome.episodes


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylestrait.donttalktunatome.R
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.FragmentEpisodesBinding
import com.kylestrait.donttalktunatome.MainActivity
import com.kylestrait.donttalktunatome.repo.DownloadRepo
import com.kylestrait.donttalktunatome.widget.BaseFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class EpisodesFragment @Inject constructor() : BaseFragment<EpisodesViewModel>(EpisodesViewModel::class.java) {
    var TAG: String? = EpisodesFragment::class.simpleName

    var mBinding: FragmentEpisodesBinding? = null
    var mRV: RecyclerView? = null
    var mAdapter: EpisodesRecyclerViewAdapter? = null

    var mItems: MutableList<Item> = ArrayList<Item>()

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

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

        mMainViewModel = (activity as MainActivity).provideMainViewModel()

        mBinding?.viewModel = mMainViewModel
        mBinding?.setLifecycleOwner { lifecycle }

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

        mMainViewModel?.mainFeed?.observe(viewLifecycleOwner, Observer {items ->
            mItems.addAll(items?.body()?.channel?.item!!)
            mAdapter?.update(items.body()?.channel?.item!!)
            mAdapter?.notifyDataSetChanged()

            Log.d(TAG, "MainFeed : " + items.body()?.channel?.item?.size)
//            context?.let { con ->
//                items.body()?.channel?.item?.get(10)?.let {
//                    viewModel?.downloadFirstEpisode(it)
//                }
//            }
        })
    }

    override fun onStop() {
        super.onStop()

        mItems.clear()
    }
}