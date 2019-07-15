package com.kylestrait.donttalktunatome.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kylestrait.donttalktunatome.data.Item

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM item")
    fun getAllDownloads(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episode: List<Item>)

    @Query("DELETE from item")
    fun deleteAllDownloads()

    @Query("DELETE from item WHERE title = :title")
    fun deleteDownloadedEpisode(title: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleDownload(item: Item)
}