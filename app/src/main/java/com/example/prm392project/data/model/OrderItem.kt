package com.example.prm392project.data.model

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val id: String,
    val orderId: String,
    val productVariantId: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotalPrice: Double,
    val createdAt: String
)

data class OrderItemResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unitPrice")
    val unitPrice: Double,
    @SerializedName("subtotalPrice")
    val subtotalPrice: Double
)