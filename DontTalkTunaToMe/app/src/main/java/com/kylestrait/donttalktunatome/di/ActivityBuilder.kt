package com.kylestrait.donttalktunatome.di

import dagger.android.ContributesAndroidInjector
import com.kylestrait.donttalktunatome.MainActivity
import dagger.Module


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBuildersModule::class))
    internal abstract fun bindMainActivity(): MainActivity

}