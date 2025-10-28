package com.example.prm392project.data.model

data class OrderItem(
    val id: String,
    val orderId: String,
    val productVariantId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotalPrice: Double,
    val createdAt: String
)
