package com.example.offlinefirst.domain.repository.auth

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.offlinefirst.db.auth.UserDao
import com.example.offlinefirst.model.User
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.network.auth.AuthService
import com.example.offlinefirst.session.SessionManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val userDao: UserDao,
    val authService: AuthService,
    val sessionManager: SessionManager
) {

    fun registerUserWithFirebase(email: String, password: String): MutableLiveData<Resource<User>> {
        val result = MediatorLiveData<Resource<User>>()
        val userApiResponse = authService.registerUserWithFirebase(email, password)
        result.addSource(userApiResponse) { userResponse ->
            result.removeSource(userApiResponse)
            if (userResponse.data == null) {
                result.value = Resource.error("Registration  failed", null)
            } else {
                Single.fromCallable {
                    val rowId: Long = userDao.insert(userResponse.data)
                    userResponse.data
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { user -> result.value = Resource.success(user) },
                        { t: Throwable -> result.value =  Resource.error("Registration  failed", null) }
                    )
            }
        }
        return result
    }

    fun signInUserWithFirebase(email: String, password: String): MutableLiveData<Resource<User>> {
        val result = MediatorLiveData<Resource<User>>()
        val userApiResponse = authService.signInUserWithFirebase(email, password)
        result.addSource(userApiResponse) { userResponse ->
            result.removeSource(userApiResponse)
            if (userResponse.data == null) {
                result.value = Resource.error("Sign in failed", null)
            } else {
                Single.fromCallable {
                    val rowId: Long = userDao.insert(userResponse.data)
                    userResponse.data
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { user -> result.value = Resource.success(user) },
                        { t: Throwable -> result.value =  Resource.error("Sign in failed", null) }
                    )
            }
        }
        return result
    }
}