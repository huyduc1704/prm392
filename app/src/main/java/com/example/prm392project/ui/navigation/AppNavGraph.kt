package com.example.prm392project.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prm392project.data.remote.AuthStore
import com.example.prm392project.data.remote.api.AddressRequest
import com.example.prm392project.data.remote.api.LoginRequest
import com.example.prm392project.data.remote.api.RegisterRequest
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
    authStore: AuthStore,
    startDestination: String = ROUTE_HOME
) {
    // compute role-based UI flag once per composition
    val role = try { authStore.getRole() ?: "" } catch (_: Exception) { "" }
    val showManage = role.equals("ADMIN", ignoreCase = true) || role.equals("MODERATOR", ignoreCase = true)

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_HOME) {
            HomeScreen(
                onProfileClick = {
                    // decide at click time using current authStore state
                    if (authStore.isAuthorized()) {
                        navController.navigate(ROUTE_PROFILE)
                    } else {
                        navController.navigate(ROUTE_LOGIN)
                    }
                },
                showManage = showManage,
                onManageClick = {
                    // admin/moderator -> management hub
                    navController.navigate(ROUTE_MANAGEMENT_HUB)
                },
                onCartClick = {
                    // regular users -> cart (no cart route defined here, implement if needed)
                }
            )
        }

        composable(ROUTE_LOGIN) {
            val repo = AuthRepository()
            val factory = AuthViewModelFactory(repo, authStore)
            val vm: AuthViewModel = viewModel(factory = factory)
            val authResp by vm.authState.collectAsState(initial = null)

            LaunchedEffect(authResp) {
                val resp = authResp
                if (resp?.isSuccessful == true) {
                    val token = resp.body()?.data?.accessToken
                    val username = resp.body()?.data?.username ?: ""
                    if (!token.isNullOrBlank()) {
                        // Persist token before navigation
                        try {
                            authStore.saveAuth(token, username)
                        } catch (_: Exception) { /* ignore */ }

                        // Navigate back to Home (clear login from back stack)
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                        }
                    }
                }
            }

            LoginScreen(
                onLoginClick = { username, password ->
                    vm.login(LoginRequest(username = username, password = password))
                },
                onRegisterClick = { navController.navigate(ROUTE_REGISTER) }
            )
        }

        composable(ROUTE_REGISTER) {
            val repo = AuthRepository()
            val factory = AuthViewModelFactory(repo, authStore)
            val vm: AuthViewModel = viewModel(factory = factory)
            val authResp by vm.authState.collectAsState(initial = null)

            LaunchedEffect(authResp) {
                val resp = authResp
                if (resp?.isSuccessful == true) {
                    // After successful register, route back to Login so user can sign in.
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_REGISTER) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                onRegisterClick = { username, email, password, phone, fullName, city, ward, street ->
                    val addr = AddressRequest(fullName = fullName, phoneNumber = phone, city = city, ward = ward, street = street)
                    val req = RegisterRequest(
                        username = username,
                        email = email,
                        password = password,
                        phoneNumber = phone,
                        addresses = listOf(addr)
                    )
                    vm.register(req)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Profile route: if authorized show ProfileScreen, otherwise redirect to login
        composable(ROUTE_PROFILE) {
            // Show ProfileScreen regardless; the screen / VM will detect unauthenticated and call back.
            ProfileScreen(
                onLogout = {
                    try { authStore.saveAuth("", "") } catch (_: Exception) { }
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_PROFILE) { inclusive = true }
                    }
                },
                onUnauthenticated = {
                    try { authStore.saveAuth("", "") } catch (_: Exception) { }
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_PROFILE) { inclusive = true }
                    }
                }
            )
        }

        composable(ROUTE_CATEGORY_MANAGEMENT) {
            if (authStore.isAuthorized()) {
                CategoryManagementScreen()
            } else {
                navController.navigate(ROUTE_NOT_AUTH) {
                    popUpTo(ROUTE_LOGIN) { inclusive = false }
                }
            }
        }

        // placeholder for management hub route (implement screen if not present)
        composable(ROUTE_MANAGEMENT_HUB) {
            Text("Management hub (implement ManagementHubScreen composable)")
        }

        composable(ROUTE_NOT_AUTH) {
            Text("You are not authorized to access this screen.")
        }
    }
}