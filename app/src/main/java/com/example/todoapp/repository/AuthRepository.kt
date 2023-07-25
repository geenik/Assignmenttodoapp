package com.example.todoapp.repository;

import android.app.Activity;
import com.example.todoapp.utils.ResultState

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import java.util.concurrent.TimeUnit;

public class AuthRepository {
    private lateinit var omVerificationCode:String

    val authdb=FirebaseAuth.getInstance()
     fun createUserWithPhone(phone: String,activity:Activity): Flow<ResultState<String>> =  callbackFlow{

        val onVerificationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0:PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0:FirebaseException) {
                trySend(ResultState.Failure(p0))
            }

            override fun onCodeSent(verificationCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, p1)
                trySend(ResultState.Success("OTP Sent Successfully"))
                omVerificationCode = verificationCode
            }

        }

        val options = PhoneAuthOptions.newBuilder(authdb)
                .setPhoneNumber("+91$phone")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallback)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose{
            close()
        }
    }

     fun signWithCredential(otp: String): Flow<ResultState<String>>  = callbackFlow{
        trySend(ResultState.Loading)
        val credential = PhoneAuthProvider.getCredential(omVerificationCode,otp)
        authdb.signInWithCredential(credential)
                .addOnCompleteListener {
            if(it.isSuccessful)
                trySend(ResultState.Success("otp verified"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }
        awaitClose {
            close()
        }
    }
}
