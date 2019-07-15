package com.kylestrait.donttalktunatome.repo

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import com.kylestrait.donttalktunatome.util.EnclosureTypeConverter
import com.kylestrait.donttalktunatome.util.ImageTypeConverter

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverter::class, EnclosureTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}