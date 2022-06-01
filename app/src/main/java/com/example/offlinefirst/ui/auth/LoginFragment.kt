package com.example.offlinefirst.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.offlinefirst.R
import com.example.offlinefirst.network.Resource
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.input_email
import kotlinx.android.synthetic.main.fragment_register.input_password
import kotlinx.android.synthetic.main.fragment_register.register_button

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : BaseAuthFragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserAuthenticationState()
        login_button.setOnClickListener {
            loginUser()
        }
    }

    private fun observeUserAuthenticationState() {
        viewModel.observeAuthState().observe(viewLifecycleOwner, Observer { response ->
            val userResource = response.getContentIfNotHandled()
            when(userResource?.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("success", "registered successfully")
                }
                Resource.Status.ERROR -> {
                    Log.d("error", "registration failed")
                }
                Resource.Status.LOADING -> {
                    Log.d("loading", "loading")
                }
            }
        })
    }

    private fun loginUser() {

        val email: String = input_email.text.toString()
        val password: String = input_password.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_LONG).show()
        } else {
            viewModel.signInUserWithFirebase(email, password)
        }
    }
}