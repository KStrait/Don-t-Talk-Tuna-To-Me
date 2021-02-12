package com.kylestrait.donttalktunatome.repo

import androidx.lifecycle.LiveData
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepo @Inject constructor(episodeDao: EpisodeDao){

    var mEpisodeDao = episodeDao

    fun saveSingleDownload(item: Item){
        CoroutineScope(Dispatchers.Default).launch {
            mEpisodeDao.insertSingleDownload(item)
        }
    }

    fun getAllDownloads(): LiveData<List<Item>> {
        return mEpisodeDao.getAllDownloads()
    }

    fun deleteDownloadedEpisode(title: String) {
        CoroutineScope(Dispatchers.Default).launch {
            mEpisodeDao.deleteDownloadedEpisode(title)
        }
    }
}