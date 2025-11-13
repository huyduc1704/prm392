package com.example.prm392project.data.model

data class OrderCartItemUi(
    val id: String,
    val productName: String,
    val color: String? = null,
    val size: String? = null,
    val unitPrice: Double,
    val quantity: Int,
    val imageUrl: String? = null
)
