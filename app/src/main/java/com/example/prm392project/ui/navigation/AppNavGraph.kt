package com.example.prm392project.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prm392project.data.local.TokenStore
import com.example.prm392project.data.remote.api.AddressRequest
import com.example.prm392project.data.remote.api.LoginRequest
import com.example.prm392project.data.remote.api.RegisterRequest
import com.example.prm392project.data.remote.api.authApiService
import com.example.prm392project.data.repository.AuthRepository
import com.example.prm392project.ui.screens.auth.AuthViewModel
import com.example.prm392project.ui.screens.auth.AuthViewModelFactory
import com.example.prm392project.ui.screens.auth.LoginScreen
import com.example.prm392project.ui.screens.auth.RegisterScreen
import com.example.prm392project.ui.screens.category.CategoryManagementScreen
import com.example.prm392project.ui.screens.main.HomeScreen
import com.example.prm392project.ui.screens.profile.ProfileScreen

const val ROUTE_HOME = "home"
const val ROUTE_LOGIN = "login"
const val ROUTE_REGISTER = "register"
const val ROUTE_MAIN = "main"
const val ROUTE_MANAGEMENT_HUB = "management_hub"
const val ROUTE_PROFILE = "profile"
const val ROUTE_CATEGORY_MANAGEMENT = "category_management"
const val ROUTE_NOT_AUTH = "not_authorized"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = ROUTE_LOGIN
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_HOME) {
            HomeScreen(
                onProfileClick = { navController.navigate(ROUTE_PROFILE) },
                onNotificationsClick = { }
            )
        }

        composable(ROUTE_LOGIN) {
            val factory = AuthViewModelFactory(AuthRepository(authApiService))
            val vm: AuthViewModel = viewModel(factory = factory)
            val loginResponse by vm.loginState.collectAsState()

            LaunchedEffect(loginResponse) {
                if (loginResponse?.isSuccessful == true) {
                    val token = loginResponse?.body()?.data?.accessToken
                    if (!token.isNullOrBlank()) {
                        TokenStore.token = token
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                            launchSingleTop = true
                        }
                        vm.clearLoginState()
                    }
                }
            }

            LoginScreen(
                onLoginClick = { username: String, password: String ->
                    vm.login(LoginRequest(username = username, password = password))
                },
                onRegisterClick = { navController.navigate(ROUTE_REGISTER) { launchSingleTop = true } }
            )
        }

        composable(ROUTE_REGISTER) {
            val factory = AuthViewModelFactory(AuthRepository(authApiService))
            val vm: AuthViewModel = viewModel(factory = factory)
            val registerResponse by vm.registerState.collectAsState()

            LaunchedEffect(registerResponse) {
                if (registerResponse?.isSuccessful == true) {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                        launchSingleTop = true
                    }
                    vm.clearRegisterState()
                }
            }

            RegisterScreen(
                onRegisterClick = { username, email, password, phone, fullName, city, ward, street ->
                    val address = AddressRequest(
                        fullName = fullName,
                        phoneNumber = phone,
                        city = city,
                        ward = ward,
                        street = street
                    )
                    vm.register(
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
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(ROUTE_PROFILE) {
            ProfileScreen(
                onLogout = {
                    TokenStore.token = null
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ROUTE_CATEGORY_MANAGEMENT) { CategoryManagementScreen() }
        composable(ROUTE_MANAGEMENT_HUB) { Text("Management hub (implement)") }
        composable(ROUTE_NOT_AUTH) { Text("You are not authorized to access this screen.") }
    }
}
