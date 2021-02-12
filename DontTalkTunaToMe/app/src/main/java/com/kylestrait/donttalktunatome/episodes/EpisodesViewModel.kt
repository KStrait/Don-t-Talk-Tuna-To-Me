package com.kylestrait.donttalktunatome.episodes

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.repo.DownloadRepo
import com.kylestrait.donttalktunatome.widget.BaseViewModel
import javax.inject.Inject

class EpisodesViewModel @Inject constructor() : BaseViewModel() {
    val TAG: String? = EpisodesViewModel::class.simpleName

    @Inject
    lateinit var downloadRepo: DownloadRepo

    companion object {
        fun create(
            fragment: Fragment,
            viewModelFactory: ViewModelProvider.Factory
        ): EpisodesViewModel {
            return ViewModelProviders.of(fragment, viewModelFactory)
                .get(EpisodesViewModel::class.java)
        }
    }

    fun downloadFirstEpisode(item: Item) {
        item.enclosure?.url?.let {
            item.title?.let { it1 -> downloadRepo.downloadEpisode(it, it1) }
        }
    }
}