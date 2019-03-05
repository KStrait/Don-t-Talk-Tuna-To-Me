package com.kylestrait.donttalktunatome.di

import com.kylestrait.donttalktunatome.episodes.EpisodesFragment
import com.kylestrait.donttalktunatome.player.PlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeEpisodesFragment(): EpisodesFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerFragment(): PlayerFragment
}