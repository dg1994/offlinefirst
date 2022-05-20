package com.example.offlinefirst.session

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.offlinefirst.db.auth.UserDao
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.model.User
import com.example.offlinefirst.utils.Event
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val userDao: UserDao,
    val application: Application) {

    private val _cachedUser = MediatorLiveData<Event<Resource<User>>>()

    val cachedUser: LiveData<Event<Resource<User>>>
        get() = _cachedUser

    fun login(source: LiveData<Event<Resource<User>>>) {
        _cachedUser.addSource(source) {
            _cachedUser.value = it
            _cachedUser.removeSource(source)
        }
    }

    fun logout() {
        val d: Disposable = Single.fromCallable{
            userDao.delete(_cachedUser.value!!.peekContent().data)
            _cachedUser.value!!.peekContent().data
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    _cachedUser.value = Event(Resource.success(null))
                },
                { t: Throwable ->
                    _cachedUser.value =  Event(Resource.error("Logout exception", null))
                }
            )
    }
}