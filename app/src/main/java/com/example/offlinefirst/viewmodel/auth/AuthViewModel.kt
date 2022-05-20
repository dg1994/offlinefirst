package com.example.offlinefirst.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.offlinefirst.domain.repository.auth.AuthRepository
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.model.User
import com.example.offlinefirst.session.SessionManager
import com.example.offlinefirst.utils.Event
import javax.inject.Inject


class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository,
    val sessionManager: SessionManager
    ) : ViewModel() {

    fun registerUserWithFirebase(email: String, password: String) {
        sessionManager.login(authRepository.registerUser(email, password))
    }

    fun signInUserWithFirebase(email: String, password: String){
        sessionManager.login(authRepository.signInUser(email, password))
    }

    fun checkPreviousAuthUser() {
        sessionManager.login(authRepository.checkPrevAuthUser())
    }

    fun observeAuthState(): LiveData<Event<Resource<User>>> {
        return sessionManager.cachedUser;
    }
}