package com.kylestrait.donttalktunatome.util

import android.arch.persistence.room.TypeConverter
import com.kylestrait.donttalktunatome.data.Image

class ImageTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toImage(image: String?): Image? {
            return if (image == null) null else Image(image)
        }

        @TypeConverter
        @JvmStatic
        fun fromImage(image: Image?): String? {
            return if (image == null) null else image.href
        }
    }
}