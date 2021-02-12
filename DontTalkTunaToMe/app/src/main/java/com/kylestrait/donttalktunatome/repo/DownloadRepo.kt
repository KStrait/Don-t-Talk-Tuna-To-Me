package com.kylestrait.donttalktunatome.repo

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.ContentUris
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.ArrayMap
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.dao.EpisodeDao
import kotlinx.coroutines.*
import java.io.File
import java.util.jar.Manifest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DownloadRepo @Inject constructor(private val context: Context, episodeDao: EpisodeDao) {
    val TAG: String? = DownloadRepo::class.simpleName

    init {
        val map = GlobalScope.launch {
            queryMediaStore29()
        }
    }

    private val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    var mEpisodeDao = episodeDao
    var downloadID: Long? = null
    private val downloadsByUri = ArrayMap<String, Long>()

    fun saveSingleDownload(item: Item) {
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
    fun downloadEpisode(url: String, title: String) {
        val fileName = "PodDon'tLie${File.separator}${url.toUri().lastPathSegment}"

        val request: DownloadManager.Request = DownloadManager.Request(url.toUri())
//            .setDestinationUri(url.toUri()) // Uri of the destination file
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(title) // Title of the Download Notification
            .setDescription("Downloading") // Description of the Download Notification
            .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
            .setAllowedOverRoaming(true) // Set if download is allowed on roaming network
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        // enqueue puts the download request in the queue.
        downloadManager.enqueue(request).also {
            downloadID = it
        }

        // using query method
        // using query method
        var finishDownload = false
        var progress: Int
        while (!finishDownload) {
            val cursor: Cursor = downloadManager.query(
                DownloadManager.Query().setFilterById(
                    downloadID!!
                )
            )
            if (cursor.moveToFirst()) {
                val status: Int =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        finishDownload = true
                    }
                    DownloadManager.STATUS_PAUSED -> {
                    }
                    DownloadManager.STATUS_PENDING -> {
                    }
                    DownloadManager.STATUS_RUNNING -> {
                        val total: Long =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (total >= 0) {
                            val downloaded: Long =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = (downloaded * 100L / total).toInt()
                            // if you use downloadmanger in async task, here you can use like this to display progress.
                            // Don't forget to do the division in long to get more digits rather than double.
                            //  publishProgress((int) ((downloaded * 100L) / total));
                        }
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true
                        Log.d(TAG, "Download Completed");
                    }
                }
            }
        }
    }

    // on Android Q/29+ we have to query/loop through MediaStore.Downloads - it should only give us the files we downloaded but won't hurt if others are returned
    @RequiresApi(29)
    private suspend fun queryMediaStore29(): Map<String, Uri> = withContext(Dispatchers.Default) {
        ArrayMap<String, Uri>().apply {

            val projection = arrayOf(
                MediaStore.Downloads._ID,
                MediaStore.Downloads.DOWNLOAD_URI
            )

            context.contentResolver.query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads._ID))
                    val uri =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.DOWNLOAD_URI))
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)

                    Log.d(TAG, "MediaStore.Downloads: $id $uri -> $contentUri")

                    put(uri, contentUri)
                }
            }
        }
    }
}