package com.example.offlinefirst.domain.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.offlinefirst.db.auth.UserDao
import com.example.offlinefirst.model.User
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.network.auth.AuthService
import com.example.offlinefirst.session.SessionManager
import com.example.offlinefirst.utils.Event
import com.example.offlinefirst.utils.PreferenceKeys
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val userDao: UserDao,
    val authService: AuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
) {

    fun registerUser(email: String, password: String): MutableLiveData<Event<Resource<User>>> {
        val result = MediatorLiveData<Event<Resource<User>>>()
        val userApiResponse = authService.registerUserWithFirebase(email, password)
        result.addSource(userApiResponse) { userResponse ->
            result.removeSource(userApiResponse)
            if (userResponse.data == null) {
                result.value = Event(Resource.error("Registration  failed", null))
            } else {
                Single.fromCallable {
                    val rowId: Long = userDao.insert(userResponse.data)
                    sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
                    sharedPrefsEditor.apply()
                    userResponse.data
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { user -> result.value = Event(Resource.success(user)) },
                        { t: Throwable -> result.value =  Event(Resource.error("Registration  failed", null)) }
                    )
            }
        }
        return result
    }

    fun signInUser(email: String, password: String): MutableLiveData<Event<Resource<User>>> {
        val result = MediatorLiveData<Event<Resource<User>>>()
        val userApiResponse = authService.signInUserWithFirebase(email, password)
        result.addSource(userApiResponse) { userResponse ->
            result.removeSource(userApiResponse)
            if (userResponse.data == null) {
                result.value = Event(Resource.error("Sign in failed", null))
            } else {
                Single.fromCallable {
                    val rowId: Long = userDao.insert(userResponse.data)
                    sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
                    sharedPrefsEditor.apply()
                    userResponse.data
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { user -> result.value = Event(Resource.success(user)) },
                        { t: Throwable -> result.value = Event(Resource.error("Sign in failed", null)) }
                    )
            }
        }
        return result
    }

    fun checkPrevAuthUser(): LiveData<Event<Resource<User>>> {
        val result = MediatorLiveData<Event<Resource<User>>>()
        val previousAuthUserEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if (previousAuthUserEmail.isNullOrBlank()) {
            result.value = Event(Resource.error("No user found", null))
        } else {
            result.addSource(userDao.searchByEmail(previousAuthUserEmail)) { user ->
                user?.let {
                    result.value = Event(Resource.success(user))
                }
            }
        }
        return result
    }
}