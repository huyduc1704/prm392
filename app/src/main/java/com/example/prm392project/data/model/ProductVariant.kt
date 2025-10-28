package com.example.prm392project.data.model

data class ProductVariant(
    val id: String,
    val productId: String,
    val color: String?,
    val size: String?,
    val price: Double,
    val stock: Int = 0,
    val createdAt: String
)
