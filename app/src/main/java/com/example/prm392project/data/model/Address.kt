package com.example.prm392project.data.model

data class Address(
    val id: String,
    val userId: String,
    val fullName: String?,
    val phoneNumber: String?,
    val city: String,
    val ward: String,
    val street: String?,
    val createdAt: String
)
