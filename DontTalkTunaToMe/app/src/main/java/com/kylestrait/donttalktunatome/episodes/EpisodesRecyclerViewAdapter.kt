package com.kylestrait.donttalktunatome.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.R
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.databinding.ItemPodcastBinding
import com.kylestrait.donttalktunatome.widget.BindingViewHolder
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

        val item = mItems[position]

        binding.myPodcast = item
        binding.viewModel = mViewModel

        pic.load(item.image?.href).fit().into(binding.image)

    }

    fun setItems(items: List<Item>?) {
        if (null != items) {
            if (mItems.isEmpty()) {
                mItems = ArrayList<Item>(items)
            }
        }
    }

    @MainThread
    fun update(newList: List<Item>) {
        if(mItems.isEmpty() || mItems == null){
            setItems(newList)
        }else {
            val result = DiffUtil.calculateDiff(
                MediaItemDiffCallback(mItems, newList), false
            )
//            mItems.clear()
//            mItems.addAll(newList)
            result.dispatchUpdatesTo(this)
        }
    }

    inner class MediaItemDiffCallback(var oldList: List<Item>?, var newList: List<Item>?) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return if (oldList == null) 0 else oldList!!.size
        }

        override fun getNewListSize(): Int {
            return if (newList == null) 0 else newList!!.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList!![oldItemPosition].title.equals(newList!![newItemPosition].title)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList!![oldItemPosition].title.equals(newList!![newItemPosition].title)
        }
    }
}