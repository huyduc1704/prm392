package com.example.prm392project.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prm392project.data.remote.api.CategoryResponse

@Composable
fun CategoryItem(
    category: CategoryResponse,
    onEdit: (CategoryResponse) -> Unit,
    onDelete: (String) -> Unit,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // name is non-null in model; keep as-is
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
                // description can be null from server — provide safe fallback
                val desc = category.description ?: ""
                if (desc.isNotBlank()) {
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                // show active state safely (CategoryResponse.active is non-null in model)
                Text(
                    text = if (category.active) "Active" else "Inactive",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            IconButton(onClick = { onEdit(category) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit category")
            }
            IconButton(onClick = { onDelete(category.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete category")
            }
        }
    }
}