package com.example.prm392project.data.model

data class Coupon(
    val id: String,
    val code: String,
    val discountType: String,
    val discountValue: Double,
    val maxUses: Int = 1,
    val currentUses: Int = 0,
    val expiresAt: String?,
    val isActive: Boolean = true,
    val createdAt: String
)
