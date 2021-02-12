package com.kylestrait.donttalktunatome.player

import android.content.Context
import android.os.Environment
import android.util.Log
import com.kylestrait.donttalktunatome.data.Item
import com.kylestrait.donttalktunatome.di.NetworkModule
import com.kylestrait.donttalktunatome.episodes.EpisodesViewModel
import com.kylestrait.donttalktunatome.manager.AudioManager
import javax.inject.Inject
import android.text.Html
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kylestrait.donttalktunatome.data.Imdb
import com.kylestrait.donttalktunatome.util.TextFormatter
import com.kylestrait.donttalktunatome.widget.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File


class PlayerViewModel @Inject constructor() : BaseViewModel() {
    val TAG: String? = PlayerViewModel::class.simpleName

    @Inject
    lateinit var api: NetworkModule

    @Inject
    lateinit var mAudioManager: AudioManager

    var imdbFeed: MutableLiveData<Response<Imdb>> = MutableLiveData()
    var posterLink: MutableLiveData<String> = MutableLiveData()
    var episodeTitle: MutableLiveData<String> = MutableLiveData()
    var isDownloaded: MutableLiveData<Boolean> = MutableLiveData()

    var textFormatter: TextFormatter = TextFormatter()

    companion object {
        fun create(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): EpisodesViewModel {
            return ViewModelProviders.of(fragment, viewModelFactory).get(EpisodesViewModel::class.java)
        }
    }

    fun setAudioManager(context: Context?, item: Item?) {
        context?.let {
            mAudioManager.setupPlayer(it, item, checkIfDownloaded(item?.title?.replace("\\s".toRegex(), "")!!))
            episodeTitle.value = item.title
        }
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

    fun checkIfDownloaded(name: String): Boolean {
        Log.d(TAG, name)
        var listFiles = mutableListOf<File>()
        listFiles.addAll(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles().toList())
        for (item in listFiles) {
            if(item.nameWithoutExtension == name) {
                Log.d(TAG, item.nameWithoutExtension)
                return true
            }else{
            }
        }
        return false
    }

    fun stripHtml(html: String): String {
        return textFormatter.stripHtml(html)
    }

    fun stripChars(title: String): String {
        return textFormatter.stripChars(title)
    }
}