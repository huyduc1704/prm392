package com.example.prm392project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prm392project.data.remote.AddressRequest
import com.example.prm392project.data.remote.AuthApiService
import com.example.prm392project.data.remote.LoginRequest
import com.example.prm392project.data.remote.RegisterRequest
import com.example.prm392project.data.remote.retrofit
import com.example.prm392project.data.repository.AuthRepository
import com.example.prm392project.ui.components.AuthViewModel
import com.example.prm392project.ui.screens.LoginScreen
import com.example.prm392project.ui.screens.RegisterScreen
import com.example.prm392project.ui.components.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val authApiService = retrofit.create(AuthApiService::class.java)
                val authRepository = AuthRepository(authApiService)
                val factory = AuthViewModelFactory(authRepository)
                val authViewModel: AuthViewModel = viewModel(factory = factory)

                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        val authState = authViewModel.authState.collectAsState()
                        LoginScreen(
                            onLoginClick = { username, password ->
                                authViewModel.login(LoginRequest(username, password))
                            },
                            onRegisterClick = { navController.navigate("register") }
                        )
                        val response = authState.value
                        if (response != null) {
                            if (response.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                // Show error message (e.g., Toast, Snackbar)
                            }
                        }
                    }
                    composable("register") {
                        val authState = authViewModel.authState.collectAsState()
                        val error = remember { mutableStateOf<String?>(null) }

                        RegisterScreen(
                            onRegisterClick = { username, email, password, phone, fullName, city, ward, street ->
                                val address = AddressRequest(
                                    fullName = fullName,
                                    phoneNumber = phone,
                                    city = city,
                                    ward = ward,
                                    street = street
                                )
                                authViewModel.register(
                                    RegisterRequest(
                                        username = username,
                                        email = email,
                                        password = password,
                                        phoneNumber = phone,
                                        role = "CUSTOMER",
                                        addresses = listOf(address)
                                    )
                                )
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                        val response = authState.value
                        if (response != null) {
                            if (response.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                try {
                                    val errorMsg = response.errorBody()?.string()
                                    error.value = if (!errorMsg.isNullOrBlank()) errorMsg else "Registration failed"
                                } catch (e: Exception) {
                                    error.value = "Registration failed"
                                }
                            }
                        }
                        error.value?.let { errMsg ->
                            Text(errMsg, color = MaterialTheme.colorScheme.error)
                        }
                    }
                    composable("home") {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Text("Welcome Home!")
                        }
                    }
                }
            }
        }
    }
}



