package com.example.prm392project.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkHeaderStart = Color(0xFF1C1C1C)
private val DarkHeaderEnd = Color(0xFF0F0F0F)
private val Accent = Color(0xFFD0875B)
private val SoftGray = Color(0xFFF3F3F3)
private val CardGray = Color(0xFFECECEC)

data class DemoProduct(
    val id: String,
    val title: String,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val products = remember {
        List(6) { i -> DemoProduct("$i", "Product ${i + 1}", "$${(20 + i * 5)}") }
    }

    Scaffold(
        bottomBar = { HomeBottomBar() },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Top dark header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DarkHeaderStart, DarkHeaderEnd)
                        )
                    )
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // two small lines simulating title/subtitle
                    Box(modifier = Modifier
                        .width(120.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF3D3D3D)))
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier
                        .width(200.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF3D3D3D)))
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Search box
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Search", color = Color.LightGray) },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.LightGray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFF222222),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        // Filter button
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Accent)
                        ) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Content column with overlap
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .padding(innerPadding)
            ) {
                // Overlapping promo card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // small pill tag
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(width = 70.dp, height = 26.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFEB6B6B))
                        )
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 18.dp)
                        ) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier
                                .width(180.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White))
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2A2A2A)))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chips row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("All", "New", "Popular", "Sale")) { text ->
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (text == "All") Accent else SoftGray)
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text, color = if (text == "All") Color.White else Color.Black, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Products grid (2 columns)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(products) { p ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier
                                    .height(120.dp)
                                    .fillMaxWidth()
                                    .background(SoftGray)
                                ) {
                                    // star badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFBDBDBD)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("★", fontSize = 14.sp, color = Color.Yellow)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                    Text(p.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Short description", color = Color.Gray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(p.price, fontWeight = FontWeight.Bold)
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Accent),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeBottomBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.Home, contentDescription = null, tint = Accent) }
                IconButton(onClick = {}) { Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Gray) }
                IconButton(onClick = {}) { Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.Gray) }
                IconButton(onClick = {}) { Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Gray) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}