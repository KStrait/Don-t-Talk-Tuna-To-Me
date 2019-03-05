package com.kylestrait.donttalktunatome.episodes

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.ItemPodcastBinding
import com.kylestrait.donttalktunatome.util.BindingViewHolder
import com.squareup.picasso.Picasso
import javax.inject.Inject

class EpisodesRecyclerViewAdapter @Inject constructor(mainViewModel: MainViewModel?) :
    RecyclerView.Adapter<BindingViewHolder>() {
    var TAG: String? = EpisodesRecyclerViewAdapter::class.simpleName

    var mItems: MutableList<Item> = ArrayList<Item>()
    val pic: Picasso = Picasso.get()
    var mViewModel: MainViewModel? = mainViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_podcast,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {

        return mItems.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val binding = holder.binding as ItemPodcastBinding
        binding.executePendingBindings()

        val item = mItems.get(position)

        binding.myPodcast = item
        binding.viewModel = mViewModel

        pic.load(item.image?.href).fit().into(binding.image)

    }

    fun setItems(items: List<Item>?) {
        if (null != items) {
            if (mItems.isEmpty()) {
                Log.d(TAG, "setItems()")
                mItems = ArrayList<Item>(items)
            }
        }
    }
}