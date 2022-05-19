package com.example.offlinefirst.network.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.offlinefirst.network.Resource
import com.example.offlinefirst.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthService
@Inject
constructor(val firebaseAuth: FirebaseAuth) {

    fun registerUserWithFirebase(email: String, password: String) : MutableLiveData<Resource<User>> {
        val authenticatedUserMutableLiveData: MutableLiveData<Resource<User>> =
            MutableLiveData<Resource<User>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register Fragment", "createUserWithEmail:success")
                    val user: FirebaseUser?  = firebaseAuth.currentUser
                    user?.let {
                        val uid: String = user.uid
                        val name: String? = user.displayName
                        val email: String? = user.email
                        val userObj = User(uid, name, email)
                        authenticatedUserMutableLiveData.setValue(Resource.success(userObj))
                    }
                } else {
                    Log.d("Register Fragment", "createUserWithEmail:failed" + task.exception)
                    authenticatedUserMutableLiveData.setValue(
                        Resource.error("Registration  failed", null)
                    )
                }
            })
        return authenticatedUserMutableLiveData;
    }

    fun signInUserWithFirebase(email: String, password: String) : MutableLiveData<Resource<User>> {
        val authenticatedUserMutableLiveData: MutableLiveData<Resource<User>> =
            MutableLiveData<Resource<User>>()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register Fragment", "signInUserWithEmail:success")
                    val user: FirebaseUser?  = firebaseAuth.currentUser
                    user?.let {
                        val uid: String = user.uid
                        val name: String? = user.displayName
                        val email: String? = user.email
                        val userObj = User(uid, name, email)
                        authenticatedUserMutableLiveData.setValue(Resource.success(userObj))
                    }
                } else {
                    Log.d("Register Fragment", "signInUserWithEmail:failed")
                    authenticatedUserMutableLiveData.setValue(
                        Resource.error("Sign In failed", null)
                    )
                }
            })
        return authenticatedUserMutableLiveData;
    }
}