package com.kylestrait.donttalktunatome.di

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMediaService(): com.kylestrait.donttalktunatome.manager.MediaService
}