package com.kylestrait.donttalktunatome.widget

import androidx.lifecycle.ViewModel
import com.kylestrait.donttalktunatome.MainViewModel

/**
 * Base class for any view model that needs access to the [MainViewModel].
 *
 * When used in conjunction with [BaseFragment] the mainViewModel field will be set automatically.
 *
 * Classes extending this must still check that mainViewModel is not null before using it.
 */
abstract class BaseViewModel : ViewModel() {
    var mainViewModel: MainViewModel? = null

    fun resume() {}

    fun pause() {}

    override fun onCleared() {
        mainViewModel = null
    }
}
