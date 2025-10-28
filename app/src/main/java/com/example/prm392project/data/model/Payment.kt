package com.example.prm392project.data.model

import com.example.prm392project.data.model.enums.PaymentStatus

data class Payment(
    val id: String,
    val orderId: String,
    val amount: Double,
    val paymentMethod: String,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val transactionId: String?,
    val bankCode: String?,
    val createdAt: String
)
