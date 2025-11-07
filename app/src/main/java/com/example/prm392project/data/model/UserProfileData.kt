package com.example.prm392project.data.model

import com.example.prm392project.data.model.enums.UserRole

data class UserProfileData(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val role: UserRole? = null,
    val addresses: List<Address> = emptyList()
)
