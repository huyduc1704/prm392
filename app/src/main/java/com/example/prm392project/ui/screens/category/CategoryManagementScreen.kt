package com.example.prm392project.ui.screens.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392project.data.remote.api.CategoryRequest
import com.example.prm392project.ui.components.CategoryItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(vm: CategoryViewModel = viewModel()) {
    val categories = vm.categories
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // form / dialog state (saved across config changes)
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }
    var nameState by rememberSaveable { mutableStateOf("") }
    var descState by rememberSaveable { mutableStateOf("") }
    var activeState by rememberSaveable { mutableStateOf(true) }

    // delete confirmation
    var deleteCandidate by rememberSaveable { mutableStateOf<String?>(null) }

    // show VM errors as snackbars
    LaunchedEffect(vm.errorMessage) {
        vm.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            vm.errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Category Management") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingId = null
                nameState = ""
                descState = ""
                activeState = true
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column {
                    if (vm.isLoading) {
                        Text("Loading...", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                    LazyColumn {
                        items(categories, key = { it.id }) { cat ->
                            CategoryItem(
                                category = cat,
                                onEdit = { c ->
                                    editingId = c.id
                                    nameState = c.name
                                    descState = c.description ?: ""
                                    activeState = c.active
                                    showDialog = true
                                },
                                onDelete = { id ->
                                    // ask for confirmation
                                    deleteCandidate = id
                                },
                                onClick = null
                            )
                        }
                    }
                }
            }
        }
    )

    // Create / Edit dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingId == null) "Create Category" else "Edit Category") },
            text = {
                Column {
                    TextField(value = nameState, onValueChange = { nameState = it }, label = { Text("Name") })
                    TextField(
                        value = descState,
                        onValueChange = { descState = it },
                        label = { Text("Description") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    TextButton(onClick = { activeState = !activeState }) {
                        Text(text = if (activeState) "Active" else "Inactive")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val req = CategoryRequest(nameState.trim(), descState.trim().ifEmpty { null }, activeState)
                        scope.launch {
                            if (editingId == null) {
                                vm.addCategory(req) { showDialog = false }
                            } else {
                                vm.updateCategory(editingId!!, req) { showDialog = false }
                            }
                        }
                    },
                    enabled = nameState.isNotBlank() // basic validation
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Delete confirmation dialog
    if (deleteCandidate != null) {
        AlertDialog(
            onDismissRequest = { deleteCandidate = null },
            title = { Text("Delete category?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    val id = deleteCandidate!!
                    deleteCandidate = null
                    vm.deleteCategory(id)
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { deleteCandidate = null }) { Text("Cancel") }
            }
        )
    }
}

