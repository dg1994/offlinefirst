package com.example.offlinefirst.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.offlinefirst.MainActivity
import com.example.offlinefirst.R
import com.example.offlinefirst.model.User
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.ui.BaseActivity
import com.example.offlinefirst.viewmodel.ViewModelProviderFactory
import com.example.offlinefirst.viewmodel.auth.AuthViewModel
import javax.inject.Inject

class AuthActivity : BaseActivity() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        subscribeObservers()
        checkPreviousAuthUser()
    }

    private fun checkPreviousAuthUser() {
        viewModel.checkPreviousAuthUser()
    }

    private fun subscribeObservers() {
        viewModel.observeAuthState().observe(this,
            Observer { response ->
                if (response.getContentIfNotHandled()?.data != null) {
                    navMainActivity()
                    finish()
                }
            })
    }


    private fun navMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}