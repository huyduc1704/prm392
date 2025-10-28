package com.example.prm392project.data.model

import com.example.prm392project.data.model.enums.OrderStatus

data class Order(
    val id: String,
    val userId: String,
    val addressId: String?,
    val totalPrice: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val orderNumber: String,
    val createdAt: String,
    val updatedAt: String,
    val completedAt: String?,
    val note: String?
)
