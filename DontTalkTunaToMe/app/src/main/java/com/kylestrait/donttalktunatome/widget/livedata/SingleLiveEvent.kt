package com.kylestrait.donttalktunatome.widget.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T> : MutableLiveData<T?>() {

    companion object {
        private val TAG = SingleLiveEvent::class.java.simpleName
        fun <T> create(value: T): SingleLiveEvent<T> {
            val singleLiveEvent: SingleLiveEvent<T> = SingleLiveEvent<T>()
            singleLiveEvent.value = value
            return singleLiveEvent
        }
    }

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
//        Log.d(TAG, "Observe");
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t: T? ->
//            Log.d(TAG, "Observed with pending: " + mPending.get());
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
//        Log.d(TAG, "Set");
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}