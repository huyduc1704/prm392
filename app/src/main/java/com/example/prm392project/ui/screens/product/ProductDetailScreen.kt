package com.example.prm392project.ui.screens.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
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
import com.example.prm392project.data.remote.api.CartItemRequest
import com.example.prm392project.ui.screens.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(productId) {
        viewModel.fetchProductDetail(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle favorite */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
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
            } else if (product != null) {
                val p = product!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(p.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(p.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                        Text("4.8 (230)", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(p.description, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Color", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        p.variants.forEach { variant ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray) // Replace with actual color
                                    .clickable { /* Handle color selection */ }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Size", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        p.variants.forEach { variant ->
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                                    .clickable { /* Handle size selection */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(variant.size)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${p.variants.firstOrNull()?.price ?: "N/A"} VND",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                val firstVariant = p.variants.firstOrNull()
                                if (firstVariant != null) {
                                    cartViewModel.addItemToCart(
                                        CartItemRequest(
                                            productVariantId = firstVariant.id,
                                            quantity = 1
                                        )
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Add to Cart")
                        }
                    }
                }
            }
        }
    }
}
