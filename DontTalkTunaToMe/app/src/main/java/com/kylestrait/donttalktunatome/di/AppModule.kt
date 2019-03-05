package com.kylestrait.donttalktunatome.di

import android.app.Application
import android.content.Context
import com.kylestrait.donttalktunatome.manager.AudioManager
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


@Module(includes = arrayOf(ViewModelModule::class, ServiceBuilderModule::class))
class AppModule {
    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }
}