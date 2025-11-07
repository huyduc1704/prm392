package com.example.prm392project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.prm392project.data.local.TokenAuthStore
import com.example.prm392project.data.remote.RetrofitInstance
import com.example.prm392project.ui.navigation.AppNavGraph
import com.example.prm392project.ui.navigation.ROUTE_LOGIN

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitInstance.init(TokenAuthStore())

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    startDestination = ROUTE_LOGIN
                )
            }
        }
    }
}