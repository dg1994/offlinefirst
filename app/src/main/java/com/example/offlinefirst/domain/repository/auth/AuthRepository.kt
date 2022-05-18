package com.example.offlinefirst.domain.repository.auth

import com.example.offlinefirst.db.auth.AccountPropertiesDao
import com.example.offlinefirst.db.auth.AuthTokenDao
import com.example.offlinefirst.network.auth.AuthApiService
import com.example.offlinefirst.session.SessionManager
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val authApiService: AuthApiService,
    val sessionManager: SessionManager
) {
}