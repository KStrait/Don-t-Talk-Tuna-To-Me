package com.kylestrait.donttalktunatome.util

import android.arch.persistence.room.TypeConverter
import com.kylestrait.donttalktunatome.data.Enclosure

class EnclosureTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toEnclosure(url: String?): Enclosure? {
            return if (url == null) null else Enclosure(url)
        }

        @TypeConverter
        @JvmStatic
        fun fromEnclosure(url: Enclosure?): String? {
            return if (url == null) null else url.url
        }
    }
}