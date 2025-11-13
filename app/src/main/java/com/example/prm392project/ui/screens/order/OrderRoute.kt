package com.example.prm392project.ui.screens.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392project.data.model.OrderCartItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderRoute(
    cartItems: List<OrderCartItemUi>,
    onOrderPlaced: () -> Unit,
    vm: OrderViewModel = viewModel()
) {
    val addresses by vm.addresses.collectAsState()
    val selectedAddressId = vm.selectedAddressId
    val isLoading = vm.isLoading
    val errorMessage = vm.errorMessage

    LaunchedEffect(Unit) {
        vm.loadProfileAndAddresses()
    }

    val totalPrice = remember(cartItems) {
        cartItems.sumOf { it.unitPrice * it.quantity }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Checkout") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select address", style = MaterialTheme.typography.titleMedium)
            if (addresses.isEmpty()) {
                Text("No addresses available")
            } else {
                addresses.forEach { addr ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAddressId == addr.id,
                            onClick = { vm.selectedAddressId = addr.id }
                        )
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(addr.fullName ?: "—", fontWeight = FontWeight.SemiBold)
                            Text(addr.phoneNumber ?: "—", style = MaterialTheme.typography.bodySmall)
                            Text(
                                listOfNotNull(addr.street, addr.ward, addr.city).joinToString(", "),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Divider()

            Text("Items", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems.size) { i ->
                    val it = cartItems[i]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(it.productName, fontWeight = FontWeight.SemiBold)
                            Text("x${it.quantity}", style = MaterialTheme.typography.bodySmall)
                        }
                        Text("${it.unitPrice * it.quantity} VND")
                    }
                }
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text("$totalPrice VND", fontWeight = FontWeight.Bold)
            }

            if (errorMessage != null) {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    vm.placeOrder(
                        totalPrice = totalPrice,
                        note = ""
                    ) { onOrderPlaced() }
                },
                enabled = selectedAddressId != null && !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text("Place order")
            }
        }
    }
}