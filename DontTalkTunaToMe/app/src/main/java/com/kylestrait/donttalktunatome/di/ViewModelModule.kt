package com.kylestrait.donttalktunatome.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.episodes.EpisodesViewModel
import com.kylestrait.donttalktunatome.menu.DownloadsViewModel
import com.kylestrait.donttalktunatome.player.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindsMainActivityViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    internal abstract fun bindsEpisodesViewModel(episodesViewModel: EpisodesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    internal abstract fun bindsPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DownloadsViewModel::class)
    internal abstract fun bindsDownloadsViewModel(downloadsViewModel: DownloadsViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}