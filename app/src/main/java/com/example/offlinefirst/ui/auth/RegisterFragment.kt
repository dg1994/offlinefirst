package com.example.offlinefirst.ui.auth

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.offlinefirst.R
import com.example.offlinefirst.network.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseAuthFragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserAuthenticationState()
        register_button.setOnClickListener {
            registerUser()
        }
    }

    private fun observeUserAuthenticationState() {
        viewModel.observeAuthState().observe(viewLifecycleOwner, Observer { response ->
            when(response.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("success", "registered successfully")
                }
                Resource.Status.ERROR -> {
                    Log.d("error", "registeration failed")
                }
                Resource.Status.LOADING -> {
                    Log.d("loading", "loading")
                }
            }
        })
    }

    private fun registerUser() {

        val email: String = input_email.text.toString()
        val password: String = input_password.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Please fill all the fields", Toast.LENGTH_LONG).show()
        } else {
            viewModel.registerUserWithFirebase(email, password)
        }
    }
}