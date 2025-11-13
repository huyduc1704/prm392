package com.example.prm392project.ui.screens.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392project.data.model.CartItem
import com.example.prm392project.data.model.OrderCartItemUi
import com.example.prm392project.data.model.OrderRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = viewModel(),
    cartItems: List<OrderCartItemUi>,  // mapped from your Cart response before navigating here
    userId: String,
    addressId: String,
    addressDisplay: String? = null,
    onOrderPlaced: () -> Unit
) {
    var note by remember { mutableStateOf("") }

    // avoid sumOf ambiguity
    val totalPrice: Double = cartItems.fold(0.0) { acc, it -> acc + (it.unitPrice * it.quantity) }

    // show backend error (optional)
    val vmError = orderViewModel.errorMessage
    LaunchedEffect(vmError) { /* hook to show snackbar if you have one */ }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Order") }) },
        bottomBar = {
            Button(
                onClick = {
                    val req = OrderRequest(
                        userId = userId,
                        addressId = addressId,
                        totalPrice = totalPrice,
                        note = note
                    )
                    orderViewModel.createOrder(req) { onOrderPlaced() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !orderViewModel.isLoading
            ) {
                if (orderViewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Place order • $totalPrice")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text("Shipping address", style = MaterialTheme.typography.titleMedium)
                Text(addressDisplay ?: "—", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                Text("Items", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
            }

            items(cartItems, key = { it.id }) { item ->
                OrderItemRow(item)
                Spacer(Modifier.height(8.dp))
            }

            item {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Note (optional)") }
                )
                Spacer(Modifier.height(12.dp))
                Text("Total: $totalPrice", style = MaterialTheme.typography.titleMedium)
                if (vmError != null) {
                    Spacer(Modifier.height(8.dp))
                    Text("Error: $vmError", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun OrderItemRow(item: OrderCartItemUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.productName, style = MaterialTheme.typography.bodyLarge)
            val size = item.size ?: "—"
            val color = item.color ?: "—"
            Text("Size: $size • Color: $color", style = MaterialTheme.typography.bodySmall)
            Text("Unit: ${item.unitPrice}", style = MaterialTheme.typography.bodySmall)
        }
        Text("x${item.quantity}", style = MaterialTheme.typography.bodyMedium)
    }
}