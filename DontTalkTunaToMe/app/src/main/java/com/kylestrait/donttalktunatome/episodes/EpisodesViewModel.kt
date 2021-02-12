package com.kylestrait.donttalktunatome.episodes

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kylestrait.donttalktunatome.widget.BaseViewModel
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(): BaseViewModel() {
    val TAG: String? = EpisodesViewModel::class.simpleName

    companion object{
        fun create(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): EpisodesViewModel {
            return ViewModelProviders.of(fragment, viewModelFactory).get(EpisodesViewModel::class.java)
        }
    }
}