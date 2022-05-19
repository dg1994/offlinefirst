package com.example.offlinefirst.session

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.offlinefirst.db.auth.UserDao
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val userDao: UserDao,
    val application: Application) {

    private val TAG: String = "AppDebug"

    private val _cachedUser = MediatorLiveData<Resource<User>>()

    val cachedUser: LiveData<Resource<User>>
        get() = _cachedUser

    fun login(source: LiveData<Resource<User>>) {
        _cachedUser.addSource(source) {
            _cachedUser.value = it
            _cachedUser.removeSource(source)
        }
    }
}