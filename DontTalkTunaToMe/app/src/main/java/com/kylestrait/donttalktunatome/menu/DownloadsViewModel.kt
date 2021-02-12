package com.kylestrait.donttalktunatome.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.repo.DownloadRepo
import javax.inject.Inject

class DownloadsViewModel @Inject constructor(): ViewModel() {

    @Inject lateinit var mDownloadRepo: DownloadRepo


    fun getAllDownloads(): LiveData<List<Item>> {
        return mDownloadRepo.getAllDownloads()
    }
}