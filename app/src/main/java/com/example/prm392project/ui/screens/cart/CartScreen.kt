package com.example.prm392project.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.prm392project.data.remote.api.CartItemResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(),
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCart()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (cart != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cart!!.items) { item ->
                            CartItemRow(item = item, viewModel = viewModel)
                        }
                    }
                    Divider()
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", fontSize = 18.sp)
                            Text("${cart!!.totalPrice} VND", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("${cart!!.totalPrice} VND", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCheckoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Proceed to Checkout")
                        }
                    }
                }
            } else {
                Text("Your cart is empty.", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItemResponse, viewModel: CartViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = ""), // Replace with actual image URL
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.productName, fontWeight = FontWeight.Bold)
            Text("${item.unitPrice} VND")
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    viewModel.updateCartItem(item.id, com.example.prm392project.data.remote.api.CartItemUpdateRequest(item.quantity - 1))
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                }
                Text(item.quantity.toString())
                IconButton(onClick = {
                    viewModel.updateCartItem(item.id, com.example.prm392project.data.remote.api.CartItemUpdateRequest(item.quantity + 1))
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
        IconButton(onClick = { viewModel.deleteCartItem(item.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
