package com.kylestrait.donttalktunatome.player

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.di.NetworkModule
import com.kylestrait.donttalktunatome.episodes.EpisodesViewModel
import com.kylestrait.donttalktunatome.manager.AudioManager
import javax.inject.Inject
import android.text.Html
import com.kylestrait.donttalktunatome.data.Imdb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File


class PlayerViewModel @Inject constructor() : ViewModel() {
    val TAG: String? = PlayerViewModel::class.simpleName

    @Inject
    lateinit var api: NetworkModule

    @Inject
    lateinit var mAudioManager: AudioManager

    var imdbFeed: MutableLiveData<Response<Imdb>> = MutableLiveData()
    var posterLink: MutableLiveData<String> = MutableLiveData()
    var episodeTitle: MutableLiveData<String> = MutableLiveData()
    var isDownloaded: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        fun create(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): EpisodesViewModel {
            return ViewModelProviders.of(fragment, viewModelFactory).get(EpisodesViewModel::class.java)
        }
    }

    fun setAudioManager(context: Context, item: Item?) {
        mAudioManager.setupPlayer(context, item)

        episodeTitle.value = item?.title
    }


    fun playPodcast() {
        mAudioManager.playPodcast()
    }

    fun getIsLoading(): MutableLiveData<Boolean>? {
        return mAudioManager.isBuffering
    }

    fun getDuration(): MutableLiveData<Int>? {
        return mAudioManager.duration
    }

    fun getPlayerStatus(): MutableLiveData<Boolean>? {
        return mAudioManager.isReady
    }

    fun getPlayingStatus(): MutableLiveData<Boolean>? {
        return mAudioManager.isPlaying
    }

    fun getImdbFeed(apiKey: String, movie: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = api.getImdbFeed("b0abd62edcb6b5521bf41469cf36797b", movie)
            val response = request.await()

            if (response.isSuccessful) {
                Log.d(TAG, "isSuccessful")
                imdbFeed.value = response

                if (response.body()?.total_results!! > 0) {
                    posterLink.value = response.body()?.results?.get(0)?.poster_path
                }
            } else {
                Log.d(TAG, "not successful")
            }
        }
    }

    fun stripHtml(html: String): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun checkIfDownloaded(name: String) {
        var listFiles = mutableListOf<File>()
        listFiles.addAll(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles().toList())
        for (item in listFiles) {
            if(item.nameWithoutExtension.equals(name)) {
                isDownloaded.value = item.nameWithoutExtension.equals(name)
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
}