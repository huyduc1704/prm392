package com.example.prm392project.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.example.prm392project.data.remote.AuthStore
import com.example.prm392project.ui.screens.management.ManagementHubScreen
import com.example.prm392project.ui.screens.profile.ProfileScreen

@Composable
fun MainScaffold(navController: NavHostController, authStore: AuthStore) {
    // determine role and whether to show Management tab
    val role = try { authStore.getRole() ?: "" } catch (_: Exception) { "" }
    val showManage = role.equals("ADMIN", ignoreCase = true) || role.equals("MANAGER", ignoreCase = true)

    // build tabs dynamically
    val tabs = if (showManage) listOf("manage" to Icons.Filled.Settings, "profile" to Icons.Filled.AccountCircle)
    else listOf("profile" to Icons.Filled.AccountCircle)

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    // keep selectedIndex valid if role/tabs change
    LaunchedEffect(tabs.size) {
        if (selectedIndex > tabs.lastIndex) selectedIndex = 0
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { idx, item ->
                    val (key, icon) = item
                    NavigationBarItem(
                        selected = selectedIndex == idx,
                        onClick = { selectedIndex = idx },
                        icon = { Icon(icon, contentDescription = key) },
                        label = { Text(if (key == "manage") "Manage" else "Profile") }
                    )
                }
            }
        }
    ) { innerPadding: PaddingValues ->
        when {
            // if manager and first tab selected -> Management hub
            showManage && selectedIndex == 0 -> ManagementHubScreen(
                onOpenCategory = { navController.navigate(ROUTE_CATEGORY_MANAGEMENT) },
                onOpenProduct = { /* TODO */ },
                onOpenOrder = { /* TODO */ },
                modifierPadding = innerPadding
            )
            // otherwise show profile
            else -> ProfileScreen(
                onLogout = {
                    // clear stored auth and navigate to login
                    try { authStore.saveAuth("", "") } catch (_: Exception) { }
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}