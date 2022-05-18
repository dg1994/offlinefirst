package com.example.offlinefirst.session

import android.app.Application
import com.example.offlinefirst.db.auth.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application) {
}