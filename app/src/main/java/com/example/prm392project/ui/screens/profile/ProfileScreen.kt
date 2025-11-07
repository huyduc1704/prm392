@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.prm392project.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392project.data.model.Address
import com.example.prm392project.data.model.UserProfileData
import com.example.prm392project.data.model.enums.UserRole

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    ProfileContent(
        profile = vm.profile,
        isLoading = vm.isLoading,
        errorMessage = vm.errorMessage,
        onRefresh = { vm.loadProfile() },
        onLogout = onLogout
    )
}

@Composable
private fun ProfileContent(
    profile: UserProfileData?,
    isLoading: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                errorMessage != null -> {
                    Column {
                        Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onRefresh) { Text("Retry") }
                    }
                }
                profile != null -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Username: ${profile.username ?: "—"}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(6.dp))
                        Text("Email: ${profile.email ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("Phone: ${profile.phoneNumber ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("Role: ${profile.role ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(12.dp))
                        Text("Addresses:", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        if (profile.addresses.isEmpty()) {
                            Text("No addresses", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn {
                                items(profile.addresses) { addr ->
                                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                        Text(addr.fullName ?: "—", style = MaterialTheme.typography.bodyLarge)
                                        Text(addr.phoneNumber ?: "—", style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            listOfNotNull(addr.street, addr.ward, addr.city).joinToString(", "),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                            Text("Logout")
                        }
                    }
                }
                else -> {
                    Column {
                        Text("No profile data")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = onRefresh) { Text("Load") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile - loaded")
@Composable
private fun ProfilePreviewLoaded() {
    ProfileContent(
        profile = UserProfileData(
            username = "john.doe",
            email = "john@example.com",
            phoneNumber = "0123456789",
            role = UserRole.ADMIN,
            addresses = emptyList<Address>()
        ),
        isLoading = false,
        errorMessage = null,
        onRefresh = {},
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Profile - loading")
@Composable
private fun ProfilePreviewLoading() {
    ProfileContent(
        profile = null,
        isLoading = true,
        errorMessage = null,
        onRefresh = {},
        onLogout = {}
    )
}

@Preview(showBackground = true, name = "Profile - error")
@Composable
private fun ProfilePreviewError() {
    ProfileContent(
        profile = null,
        isLoading = false,
        errorMessage = "Failed to load profile",
        onRefresh = {},
        onLogout = {}
    )
}
