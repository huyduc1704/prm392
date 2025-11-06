package com.example.prm392project.ui.screens.management

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagementHubScreen(
    onOpenCategory: () -> Unit,
    onOpenProduct: () -> Unit,
    onOpenOrder: () -> Unit,
    modifierPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(modifierPadding)
            .padding(16.dp)
    ) {
        Text("Management", modifier = Modifier.padding(bottom = 12.dp))
        Button(onClick = onOpenCategory, modifier = Modifier.padding(vertical = 6.dp)) {
            Text("Category Management")
        }
        Button(onClick = onOpenProduct, modifier = Modifier.padding(vertical = 6.dp)) {
            Text("Product Management")
        }
        Button(onClick = onOpenOrder, modifier = Modifier.padding(vertical = 6.dp)) {
            Text("Order Management")
        }
    }
}