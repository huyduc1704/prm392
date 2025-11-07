package com.example.prm392project.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel

private val DarkHeaderStart = Color(0xFF1C1C1C)
private val DarkHeaderEnd = Color(0xFF0F0F0F)
private val Accent = Color(0xFFD0875B)
private val CardGray = Color(0xFFECECEC)
private val ScreenBg = Color(0xFFF6F6F8)
private val BadgeRed = Color(0xFFFF6B6B)

data class DemoProduct(val id: String, val title: String, val price: String)

@Composable
@Preview
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val fullName by viewModel.fullName.collectAsState()
    val categories by viewModel.categories.collectAsState(initial = listOf("All"))
    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
        viewModel.fetchCategories()
    }

    val isLoading = fullName == "Loading..."
    val isError = fullName.startsWith("Error")

    val products = remember { List(6) { i -> DemoProduct("$i", "Product ${i + 1}", "$${20 + i * 5}") } }
    var search by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(0) }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val extraTopGap = 0.dp
    val headerTopGap = statusBarTop + extraTopGap

    // tuned sizes: increase headerHeight so search sits above the overlap
    val headerHeight = 220.dp
    val promoHeight = 140.dp
    val halfPromo = promoHeight / 2
    val headerInnerHeight = headerHeight - halfPromo // reserved area above the promo overlap

    Scaffold(
        bottomBar = { HomeBottomBar(onProfileClick) },
        containerColor = ScreenBg,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header background (full headerHeight, promo will overlap its bottom)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(brush = Brush.verticalGradient(listOf(
                        DarkHeaderStart,
                        DarkHeaderEnd
                    )))
            ) {
                // top area reserved so promo doesn't cover the search
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerInnerHeight) // content lives above the overlapping promo
                ) {
                    // greeting & name at top-left
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text(
                                text = "Xin chào",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = if (fullName.isBlank()) "–" else fullName,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // search row placed at the bottom of the reserved inner area
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            placeholder = { Text("Search", color = Color.LightGray, fontSize = 15.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF222222),
                                unfocusedContainerColor = Color(0xFF222222),
                                disabledContainerColor = Color(0xFF222222),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                cursorColor = Color.White
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = { /* filter */ },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Accent)
                        ) {
                            Icon(Icons.Default.List, contentDescription = "Filter", tint = Color.White)
                        }
                    }
                }

                // notification icon stays at top right of header background
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 22.dp, end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.Gray
                    )
                }
            }

            // Content: start exactly below header, then push chips down so they are below promo bottom
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = headerTopGap + headerHeight) // start right after header bottom
                    .background(ScreenBg)
            ) {

                Spacer(Modifier.height(halfPromo + 8.dp)) // half promo (intrusion) + small gap

                CategoryChipsRow(
                    categories = categories,
                    selectedIndex = selectedCat,
                    onSelect = { selectedCat = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(products.size) { idx ->
                        val p = products[idx]
                        ProductCard(
                            title = p.title,
                            price = p.price,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Promo drawn last so it sits above both header and content; positioned so half overlaps
            FeaturePromoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(promoHeight)
                    .align(Alignment.TopCenter)
                    .offset(y = headerTopGap + headerHeight - halfPromo)
                    .zIndex(1f)
            )
        }
    }
}

@Composable
private fun FeaturePromoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                // small promo badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BadgeRed)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "Promo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Buy one get\none FREE",
                    color = Color(0xFF222222),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Limited time offer",
                    color = Color(0xFF7A7A7A),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            // image placeholder and heart icon to emulate reference look
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 92.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFFEFEFEF), Color(0xFFF7F7F7))))
            ) {
                // floating heart badge
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFEDE6))
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChipsRow(
    categories: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEachIndexed { i, label ->
            val selected = i == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) Accent else Color.White)
                    .borderIf(!selected, 1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .clickable { onSelect(i) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    color = if (selected) Color.White else Color(0xFF323232),
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProductCard(title: String, price: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(CardGray)
            )
            Box(
                Modifier
                    .padding(8.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Accent)
                    .align(Alignment.TopEnd)
            )
            Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                Text(title, color = Color.Black, fontSize = 14.sp, maxLines = 1)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(price, color = Accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Accent)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeBottomBar(onProfileClick: () -> Unit) {
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
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) { Icon(Icons.Default.Home, contentDescription = null, tint = Accent) }
                IconButton(onClick = {}) { Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Gray) }
                IconButton(onClick = {}) { Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.Gray) }
                IconButton(onClick = onProfileClick) { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Gray) }
            }
        }
    }
}

// small helper to avoid an extra dependency
private fun Modifier.borderIf(condition: Boolean, width: Dp, color: Color, shape: RoundedCornerShape) =
    if (condition) this.then(Modifier.border(width, color, shape)) else this
