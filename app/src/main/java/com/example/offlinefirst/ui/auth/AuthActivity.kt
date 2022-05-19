package com.example.offlinefirst.ui.auth

import android.os.Bundle
import com.example.offlinefirst.R
import com.example.offlinefirst.ui.BaseActivity
import dagger.android.support.DaggerAppCompatActivity

class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}