package com.kylestrait.donttalktunatome.repo

import android.arch.lifecycle.LiveData
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepo @Inject constructor(episodeDao: EpisodeDao){

    var mEpisodeDao = episodeDao

    fun saveDownloads(episodes: List<Item>) {
        GlobalScope.launch(Dispatchers.Default) {
            mEpisodeDao.insertAll(episodes)
        }
    }

    fun saveSingleDownload(item: Item){
        GlobalScope.launch(Dispatchers.Default) {
            mEpisodeDao.insertSingleDownload(item)
        }
    }

    fun deleteDownloads(episodes: List<Item>) {
        GlobalScope.launch(Dispatchers.Default) {
            mEpisodeDao.deleteAllDownloads()
        }
    }

    fun getAllDownloads(): LiveData<List<Item>> {
        return mEpisodeDao.getAllDownloads()
    }

    fun deleteDownloadedEpisode(title: String) {
        GlobalScope.launch(Dispatchers.Default) {
            mEpisodeDao.deleteDownloadedEpisode(title)
        }
    }
}