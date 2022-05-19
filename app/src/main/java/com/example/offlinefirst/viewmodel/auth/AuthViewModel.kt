package com.example.offlinefirst.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.offlinefirst.domain.repository.auth.AuthRepository
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.model.User
import com.example.offlinefirst.session.SessionManager
import javax.inject.Inject


class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository,
    val sessionManager: SessionManager
    ) : ViewModel() {

    fun registerUserWithFirebase(email: String, password: String) {
        sessionManager.login(authRepository.registerUserWithFirebase(email, password))
    }

    fun signInUserWithFirebase(email: String, password: String){
        sessionManager.login(authRepository.signInUserWithFirebase(email, password))
    }

    fun observeAuthState(): LiveData<Resource<User>> {
        return sessionManager.cachedUser;
    }

}