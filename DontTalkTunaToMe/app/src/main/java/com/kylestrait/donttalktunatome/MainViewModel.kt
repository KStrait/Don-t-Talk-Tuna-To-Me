package com.kylestrait.donttalktunatome

import android.arch.lifecycle.*
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.data.RSS
import com.kylestrait.donttalktunatome.di.NetworkModule
import com.kylestrait.donttalktunatome.manager.AudioManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    var mainFeed: MutableLiveData<Response<RSS>> = MutableLiveData()
    var episode: MutableLiveData<Item> = MutableLiveData()
    var epTitle: String? = null
    var getIsPlaying: MutableLiveData<Boolean> = MutableLiveData()
    var downloadProgress: MutableLiveData<Int> = MutableLiveData()
    var downloading: MutableLiveData<Boolean> = MutableLiveData()
    var isDownloaded: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        fun create(activity: FragmentActivity, viewModelFactory: ViewModelProvider.Factory): MainViewModel {
            return ViewModelProviders.of(activity, viewModelFactory).get(MainViewModel::class.java)
        }
    }

    fun getMainFeed() {
        GlobalScope.launch(Dispatchers.Main) {
            val request = api.getRssFeed()
            val response = request.await()

            if (response.isSuccessful) {
                Log.d(TAG, "isSuccessful")

                mainFeed.value = response
            } else {
                Log.d(TAG, "not successful")
            }
        }
    }

    fun setEpisode(item: Item) {
        episode.value = item
        epTitle = item.title?.replace(" ", "")
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

        GlobalScope.launch(Dispatchers.Main) {
            val request = api.getDownload(result)
            val response = request.await()

            if (response.isSuccessful) {

                downloading.value = true
                saveToDisk(response.body()!!)
            } else {
                Log.d(TAG, "download not reached")
            }
        }
    }

    fun checkIfDownloaded(name: String) {
        var listFiles = mutableListOf<File>()
        listFiles.addAll(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles().toList())
        for (item in listFiles) {
            if(item.nameWithoutExtension.equals(name)) {
                Log.d(TAG, "RIGHT HERE")
                isDownloaded.value = item.nameWithoutExtension.equals(name)
                return
            }else{
                isDownloaded.value = false
            }
        }
    }

    fun deleteEpisodeFromStorage(title: String){
        var newTitle: String? =  title.replace("\\s".toRegex(), "")
        var listFiles = mutableListOf<File>()
        listFiles.addAll(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles().toList())
        for (item in listFiles) {
            if(item.nameWithoutExtension.equals(newTitle)) {
                var newFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), newTitle.plus(".mp3"))
                if(newFile.exists()) {
                    newFile.delete()
                    Log.d(TAG, "DELETE")
                    isDownloaded.value = false
                }
            }
        }
    }

    fun saveToDisk(body: ResponseBody) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                var newFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), epTitle.plus(".mp3"))

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
                    if (`is` != null) `is`!!.close()
                    if (os != null) os!!.close()

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
}