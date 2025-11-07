package com.example.prm392project.data.model

data class CartItem(
    val id: String,
    val cartId: String,
    val productVariantId: String,
    val quantity: Int,
    val uintPrice: Double = 0.0,
    val createdAt: String
)
data class Cart(
    val id: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
)
