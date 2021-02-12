package com.kylestrait.donttalktunatome

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.RSS
import com.kylestrait.donttalktunatome.di.NetworkModule
import com.kylestrait.donttalktunatome.manager.AudioManager
import com.kylestrait.donttalktunatome.repo.DownloadRepo
import com.kylestrait.donttalktunatome.widget.livedata.SingleLiveEvent
import kotlinx.coroutines.*
import retrofit2.Response
import java.text.SimpleDateFormat
import javax.inject.Inject
import okhttp3.ResponseBody
import java.io.*

class MainViewModel @Inject constructor() : ViewModel() {
    val TAG: String? = MainViewModel::class.simpleName

    @Inject
    lateinit var api: NetworkModule

    @Inject
    lateinit var mAudioManager: AudioManager

    @Inject
    lateinit var downloadRepo: DownloadRepo

    var mainFeed: SingleLiveEvent<Response<RSS>> = SingleLiveEvent()
    var episode: MutableLiveData<Item> = MutableLiveData()
    var epTitle: String? = null
    var getIsPlaying: MutableLiveData<Boolean> = MutableLiveData()
    var downloadProgress: MutableLiveData<Int> = MutableLiveData()
    var downloading: MutableLiveData<Boolean> = MutableLiveData()
    var isDownloaded: MutableLiveData<Boolean> = MutableLiveData()
    var deleteDownload: MutableLiveData<Boolean> = MutableLiveData()
    var isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    var showDownloads: MutableLiveData<Boolean> = MutableLiveData()
    var permission: Boolean = false

    companion object {
        fun create(
            activity: FragmentActivity,
            viewModelFactory: ViewModelProvider.Factory
        ): MainViewModel {
            return ViewModelProviders.of(activity, viewModelFactory).get(MainViewModel::class.java)
        }
    }

    fun getMainFeed() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = api.getRssFeed()
            val response = request.await()
            if (response.isSuccessful) {
                Log.d(TAG, "getMainFeed() isSuccessful")
                mainFeed.postValue(response)
                isRefreshing.postValue(false)
            } else {
                Log.d(TAG, "getMainFeed() not successful")
                isRefreshing.postValue(false)
            }
        }
    }

    fun reloadFeed() {
        isRefreshing.value = true

        viewModelScope.launch(Dispatchers.Default) {
            val request = api.getRssFeed()
            val response = request.await()
            if (response.isSuccessful) {
                Log.d(TAG, "isSuccessful")
                mainFeed.postValue(response)
                isRefreshing.postValue(false)
            } else {
                Log.d(TAG, "not successful")
                isRefreshing.postValue(false)
            }
        }
    }

    fun setEpisode(item: Item) {
        episode.value = item
        epTitle = item.title?.replace(" ", "")
        checkIfDownloaded(epTitle!!)
    }

    fun getIsPlaying(): MutableLiveData<Boolean> {
        return getIsPlaying
    }

    fun formatEpisodeDate(dateInput: String): String {
        var format = SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z")
        val newDate = format.parse(dateInput)

        format = SimpleDateFormat("MMM dd, yyyy hh:mm a")
        val date = format.format(newDate)

        return date
    }


    fun downloadCurrentEpisode(url: String) {
        var result: String = url.substring(url.lastIndexOf(',') + 1).trim()

        viewModelScope.launch(Dispatchers.Main) {
            val request = api.getDownload(result)
            val response = request.await()

            if (response.isSuccessful) {

                downloading.value = true
                saveToDisk(response.body()!!)

                downloadRepo.saveSingleDownload(episode.value!!)
            } else {
                Log.d(TAG, "download not reached")
            }
        }
    }

    fun checkIfDownloaded(name: CharSequence) {
        if (permission) {
            var listFiles = mutableListOf<File>()
            listFiles.addAll(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .listFiles().toList()
            )
            listFiles.let {
                if (!it.isEmpty()) {
                    for (item in it) {
                        if (item.nameWithoutExtension == name) {
                            isDownloaded.value = true
                            return
                        } else {
                            isDownloaded.value = false
                        }
                    }
                }
            }
        } else {
            Log.d(TAG, "permission not yet granted")
        }
    }

    fun deleteEpisodeFromStorage(title: String) {
        var newTitle: String? = title.replace("\\s".toRegex(), "")
        var listFiles = mutableListOf<File>()
        listFiles.addAll(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .listFiles().toList()
        )
        for (item in listFiles) {
            if (item.nameWithoutExtension.equals(newTitle)) {
                var newFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    newTitle.plus(".mp3")
                )
                if (newFile.exists()) {
                    newFile.delete()
                    downloadRepo.deleteDownloadedEpisode(title)
                    isDownloaded.value = false
                }
            }
        }
    }

    fun saveToDisk(body: ResponseBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var newFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    epTitle.toString().plus(".mp3")
                )

                var `is`: InputStream? = null
                var os: OutputStream? = null

                try {
                    Log.d(TAG, "File Size=" + body.contentLength())

                    `is` = body.byteStream()
                    os = FileOutputStream(newFile)

                    val data = ByteArray(4096)
                    var count: Int
                    var progress = 0
                    count = `is`.read(data)

                    while (count != -1) {
                        os.write(data, 0, count)
                        progress += count
//                    Log.d(
//                        TAG,
//                        "Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress.toFloat() / body.contentLength()
//                    )
                        count = `is`.read(data)

                        downloadProgress.postValue(((progress.toFloat() / body.contentLength()) * 100).toInt())
                    }

                    os.flush()
                    Log.d(TAG, "File saved successfully!")
//                    return
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.d(TAG, "Failed to save the file!")
//                    return
                } finally {
                    if (`is` != null) `is`.close()
                    if (os != null) os.close()

                    Log.d(TAG, "File saved successfully! 2")
                    downloading.postValue(false)
                    isDownloaded.postValue(true)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "Failed to save the file!")
//                return
            }
        }
    }

    fun onLongClick(title: String): Boolean {
//        deleteDownload.value = true
        deleteEpisodeFromStorage(title)
        return false
    }


    fun showDownloads(show: Boolean) {
        showDownloads.value = show
    }

    fun getAllDownloads(): MutableList<File> {
        var listFiles = mutableListOf<File>()
        listFiles.addAll(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .listFiles().toList()
        )
        return listFiles
    }
}