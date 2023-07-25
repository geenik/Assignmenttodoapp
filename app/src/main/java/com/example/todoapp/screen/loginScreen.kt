package com.example.todoapp.screen

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todoapp.components.CommonDialog
import com.example.todoapp.components.OtpView
import com.example.todoapp.utils.ResultState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    activity: ComponentActivity
) {
    val context=LocalContext.current
    val viewModel:LoginViewModal= viewModel()
    val mobile = remember { mutableStateOf("") }
    val otp = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isDialog = remember { mutableStateOf(false) }
    val keyboardController=LocalSoftwareKeyboardController.current
    if(isDialog.value) CommonDialog()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Enter Mobile Number")
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(value = mobile.value, onValueChange = {mobile.value=it},
                modifier = Modifier.fillMaxWidth(), label = {
                    Text(text = "+91")
                }, keyboardActions = KeyboardActions(
                    onDone = {
                        // Hide the keyboard when the keyboard action (e.g., Done) is pressed
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Set the keyboard action (e.g., Done)
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                scope.launch(Dispatchers.Main){
                    if(mobile.value.trim().length==10) {
                        viewModel.createUserWithPhone(
                            mobile.value,
                            activity
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isDialog.value = false
                                }
                                is ResultState.Failure -> {
                                    isDialog.value = false
                                    Toast.makeText(context,it.msg.toString(),Toast.LENGTH_SHORT).show()
                                }

                                ResultState.Loading -> {
                                    isDialog.value = true
                                }
                            }
                        }
                    }
                }

            }) {
                Text(text = "Submit")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Enter Otp")
            Spacer(modifier = Modifier.height(20.dp))
            OtpView(otpText = otp.value) {
                otp.value= it
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                scope.launch(Dispatchers.Main){
                    if(otp.value.length==6) {
                        viewModel.signInWithCredential(
                            otp.value
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isDialog.value = false
                                    navController.navigate("todoScreen")
                                    navController.popBackStack()
                                    Log.d("deb", it.data.toString())
                                }

                                is ResultState.Failure -> {
                                    isDialog.value = false
                                    Toast.makeText(context, it.msg.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("deb", it.msg.toString())
                                }
                                ResultState.Loading -> {
                                    isDialog.value = true
                                }
                            }
                        }
                    }
                }
            }) {
                Text(text = "Verify")
            }
        }
    }

}

