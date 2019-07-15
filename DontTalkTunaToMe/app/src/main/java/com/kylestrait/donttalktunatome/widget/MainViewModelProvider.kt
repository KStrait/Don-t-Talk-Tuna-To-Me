package com.kylestrait.donttalktunatome.widget

import com.kylestrait.donttalktunatome.MainViewModel

interface MainViewModelProvider {
    fun provideMainViewModel(): MainViewModel
}
