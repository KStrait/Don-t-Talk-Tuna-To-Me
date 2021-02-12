package com.kylestrait.donttalktunatome.repo

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DownloadRepo @Inject constructor(episodeDao: EpisodeDao){

    var mEpisodeDao = episodeDao
    var downloadID : Long? = null

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

    //todo mediastore stuff
//    fun downloadEpisode(context : Context) {
//        val url = "http://speedtest.ftp.otenet.gr/files/test10Mb.db"
//        var fileName = url.substring(url.lastIndexOf('/') + 1)
//        fileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1)
//        val file: File = Util.createDocumentFile(fileName, context)
//
//        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN) // Visibility of the download Notification
//            .setDestinationUri(Uri.fromFile(file)) // Uri of the destination file
//            .setTitle(fileName) // Title of the Download Notification
//            .setDescription("Downloading") // Description of the Download Notification
//            .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
//            .setAllowedOverRoaming(true) // Set if download is allowed on roaming network
//
//        val downloadManager: DownloadManager? = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//        downloadID =
//            downloadManager?.enqueue(request) // enqueue puts the download request in the queue.
//
//    }
}