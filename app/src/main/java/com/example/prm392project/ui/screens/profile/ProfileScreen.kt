package com.example.prm392project.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val profile = vm.profile
    val isLoading = vm.isLoading
    val error = vm.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { vm.loadProfile() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                    }
                    error != null -> {
                        Column {
                            Text("Error: $error", color = MaterialTheme.colorScheme.error)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { vm.loadProfile() }) { Text("Retry") }
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
                                            Text(listOfNotNull(addr.street, addr.ward, addr.city).joinToString(", "), style = MaterialTheme.typography.bodySmall)
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
                            Button(onClick = { vm.loadProfile() }) { Text("Load") }
                        }
                    }
                }
            }
        }
    )
}