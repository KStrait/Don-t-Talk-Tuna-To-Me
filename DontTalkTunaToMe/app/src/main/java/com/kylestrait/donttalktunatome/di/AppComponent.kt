package com.kylestrait.donttalktunatome.di

import com.kylestrait.donttalktunatome.TunaApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class))
interface AppComponent : AndroidInjector<TunaApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<TunaApplication>()
}