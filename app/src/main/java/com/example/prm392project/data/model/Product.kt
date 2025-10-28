package com.example.prm392project.data.model

data class Product(
    val id: String,
    val categoryId: String?,
    val name: String,
    val description: String?,
    val brand: String?,
    val imageUrl: String?,
    val isActive: Boolean = true,
    val updatedAt: String,
    val createdAt: String
)
