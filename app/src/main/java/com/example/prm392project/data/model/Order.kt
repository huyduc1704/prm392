package com.example.prm392project.data.model

import com.example.prm392project.data.model.enums.OrderStatus
import com.google.gson.annotations.SerializedName
import java.util.Date

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

data class OrderResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("orderNumber")
    val orderNumber: String,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("createdAt")
    val createdAt: Date,
    @SerializedName("updatedAt")
    val updatedAt: Date,
    @SerializedName("completedAt")
    val completedAt: Date?,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("ward")
    val ward: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("items")
    val items: List<OrderItemResponse>
)

data class OrderRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("addressId")
    val addressId: String,
    @SerializedName("totalPrice")
    val totalPrice: Double,
    @SerializedName("note")
    val note: String
)