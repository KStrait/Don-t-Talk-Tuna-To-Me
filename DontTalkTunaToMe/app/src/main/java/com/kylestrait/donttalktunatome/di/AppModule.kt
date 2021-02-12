package com.kylestrait.donttalktunatome.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kylestrait.donttalktunatome.TunaApplication
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import com.kylestrait.donttalktunatome.repo.AppDatabase
import com.kylestrait.donttalktunatome.repo.DownloadRepo
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


@Module(includes = arrayOf(ViewModelModule::class, ServiceBuilderModule::class))
class AppModule {

//    private var application: Application? = null
//
//    fun AppModule(application: Application) {
//        this.application = application
//    }


    @Provides
    @Singleton
    internal fun providesContext(application: TunaApplication): Context {
        return application
    }

    @Singleton
    @Provides fun providesAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "my-app-db").build()
    }

    @Singleton
    @Provides fun providesEpisodeDao(database: AppDatabase): EpisodeDao {
        return database.episodeDao()
    }

    @Singleton
    @Provides
    fun providesDownloadRepo(context: Context, episodeDao: EpisodeDao): DownloadRepo{
        return DownloadRepo(context, episodeDao)
    }
}