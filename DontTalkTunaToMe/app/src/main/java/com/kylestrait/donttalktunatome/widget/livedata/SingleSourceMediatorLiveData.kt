package com.kylestrait.donttalktunatome.widget.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer


class SingleSourceMediatorLiveData<T> : MediatorLiveData<T>() {
    private var source: LiveData<*>? = null
    fun <S> setSource(source: LiveData<S>, onChanged: Observer<in S>) {
        if (this.source != null) removeSource(this.source!!)
        this.source = source
        super.addSource(source, onChanged)
    }
}