package com.kylestrait.donttalktunatome.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kylestrait.donttalktunatome.MainActivity
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.FragmentDownloadsBinding
import com.kylestrait.donttalktunatome.episodes.EpisodesRecyclerViewAdapter
import com.kylestrait.donttalktunatome.episodes.EpisodesViewModel
import com.kylestrait.donttalktunatome.util.ItemTouchHelperCallback
import com.kylestrait.donttalktunatome.util.SwipeToDeleteHandler
import dagger.android.support.DaggerFragment
import java.io.File
import javax.inject.Inject

class DownloadsFragment @Inject constructor() : DaggerFragment(), SwipeToDeleteHandler.RecyclerItemTouchHelperListener {
    var TAG: String? = DownloadsFragment::class.simpleName

    var mBinding: FragmentDownloadsBinding? = null
    var mMainViewModel: MainViewModel? = null
    var mViewModel: DownloadsViewModel? = null

    var mRV: RecyclerView? = null
    var mAdapter: DownloadsRecyclerViewAdapter? = null

    var mItems: MutableList<Item> = ArrayList<Item>()

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mList: MutableList<File>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mViewModel = ViewModelProviders.of(
            this,
            mViewModelFactory
        ).get(DownloadsViewModel::class.java)

        mMainViewModel = (activity as MainActivity).provideMainViewModel()

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloads, container, false)
        mBinding?.executePendingBindings()

        mBinding?.viewModel = mMainViewModel

        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (mBinding?.downloadsRv != null) {
            mRV = mBinding?.downloadsRv
            val context: Context? = mRV?.context

            mRV?.setHasFixedSize(true)

            mAdapter = DownloadsRecyclerViewAdapter(mMainViewModel)

            mRV?.adapter = mAdapter

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(
                mRV?.context,
                layoutManager.orientation
            )
            mRV?.addItemDecoration(dividerItemDecoration)
            mRV?.layoutManager = layoutManager

            val itemTouchHelperCallback =
                SwipeToDeleteHandler(this)
//            val x = ItemTouchHelperCallback()
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRV)
        }

        mViewModel?.getAllDownloads()?.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.size.toString())
            mItems.addAll(it!!)
            mAdapter?.setItems(it)
            mAdapter?.notifyDataSetChanged()
        })
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        mAdapter?.removeItem(position)
        mAdapter?.notifyItemRemoved(position)
        mMainViewModel?.deleteEpisodeFromStorage(mItems[position].title!!.toString())
    }

    override fun onBeginSwipe(direction: Float, viewHolder: RecyclerView.ViewHolder) {
        Log.d(TAG, "beginSwipe")
    }

}