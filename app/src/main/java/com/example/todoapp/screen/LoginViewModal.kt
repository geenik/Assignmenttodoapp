package com.example.todoapp.screen

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.todoapp.repository.AuthRepository

class LoginViewModal  : ViewModel() {
   val repo=AuthRepository()
    fun createUserWithPhone(
        mobile:String,
        activity: Activity
    ) = repo.createUserWithPhone(mobile,activity)

    fun signInWithCredential(
        code:String
    ) = repo.signWithCredential(code)

}