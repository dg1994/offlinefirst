package com.example.offlinefirst.viewmodel.auth

import androidx.lifecycle.ViewModel
import com.example.offlinefirst.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(val authRepository: AuthRepository) : ViewModel() {
}