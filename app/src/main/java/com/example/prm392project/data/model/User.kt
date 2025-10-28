package com.example.prm392project.data.model

import com.example.prm392project.data.model.enums.UserRole

data class User (
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val phoneNumber: String,
    val role: UserRole = UserRole.CUSTOMER,
    val isActive: Boolean = true,
    val updatedAt: String,
    val createdAt: String
)

