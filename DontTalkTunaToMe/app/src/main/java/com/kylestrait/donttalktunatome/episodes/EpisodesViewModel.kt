package com.kylestrait.donttalktunatome.episodes

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.kylestrait.donttalktunatome.MainViewModel
import com.kylestrait.donttalktunatome.data.RSS
import com.kylestrait.donttalktunatome.di.NetworkModule
import com.kylestrait.donttalktunatome.manager.AudioManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class EpisodesViewModel @Inject constructor(): ViewModel() {
    val TAG: String? = EpisodesViewModel::class.simpleName

    @Inject lateinit var api: NetworkModule
    @Inject lateinit var audioManager: AudioManager


    var mainFeed: MutableLiveData<Response<RSS>> = MutableLiveData()
    var isPlaying: MutableLiveData<Boolean> = MutableLiveData()

    companion object{
        fun create(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): EpisodesViewModel {
            return ViewModelProviders.of(fragment, viewModelFactory).get(EpisodesViewModel::class.java)
        }
    }

    fun getMainFeed(){
        GlobalScope.launch(Dispatchers.Main) {
            val request = api.getRssFeed()
            val response = request.await()

            if(response.isSuccessful){
                Log.d(TAG, "isSuccessful")

                mainFeed.value = response
            }else{
                Log.d(TAG, "not successful")
            }
        }
    }

    fun getIsPlaying(){
        if(audioManager.exoPlayer?.playWhenReady == true){
            isPlaying.value = true
        }else{
            isPlaying.value = false
        }
    }

}