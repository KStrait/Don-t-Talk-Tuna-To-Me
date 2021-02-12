package com.kylestrait.donttalktunatome.data.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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