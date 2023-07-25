package com.example.todoapp.navigation

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.screen.LoginScreen
import com.example.todoapp.screen.TodoScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoNavigation (activity: ComponentActivity){
    val auth=Firebase.auth
    val navController
            = rememberNavController()
    NavHost(navController = navController, startDestination =if(auth.currentUser!=null) {
        "todoScreen"
    } else{
        "loginScreen"
    }){
        composable("loginScreen"){
            LoginScreen(navController=navController,activity)
        }
        composable("todoScreen"){
            TodoScreen(navController=navController)
        }
    }
}