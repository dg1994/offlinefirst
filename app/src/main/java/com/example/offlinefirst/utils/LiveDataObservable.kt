package com.example.offlinefirst.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables
import android.os.Looper

class LiveDataObservable<T>(
        private val liveData: LiveData<T>,
        private val valueIfNull: T? = null
) : Observable<T>() {

    override fun subscribeActual(observer: io.reactivex.Observer<in T>) {
        if (!checkMainThread(observer)) {
            return
        }
        val relay = RemoveObserverInMainThread(observer)
        observer.onSubscribe(relay)
        liveData.observeForever(relay)
    }

    private inner class RemoveObserverInMainThread(private val observer: io.reactivex.Observer<in T>)
        : MainThreadDisposable(), Observer<T> {

        override fun onChanged(t: T?) {
            if (!isDisposed) {
                if (t == null) {
                    if (valueIfNull != null) {
                        observer.onNext(valueIfNull)
                    } else {
                        observer.onError(ReactiveStreamNullElementException(
                                "convert liveData value t to RxJava onNext(t), t cannot be null"))
                    }
                } else {
                    observer.onNext(t)
                }
            }
        }

        override fun onDispose() {
            liveData.removeObserver(this)
        }
    }

    internal fun checkMainThread(observer: io.reactivex.Observer<*>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().name))
            return false
        }
        return true
    }

    class ReactiveStreamNullElementException(detail: String) : NullPointerException(detail)
}