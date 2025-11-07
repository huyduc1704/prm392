package com.example.prm392project.data.repository

import com.example.prm392project.data.model.User
import com.example.prm392project.data.model.Address
import com.example.prm392project.data.model.Cart
import com.example.prm392project.data.remote.ApiResponse

interface ProfileRepository {
    suspend fun getProfile(): ApiResponse<User>
    suspend fun getAddresses(): ApiResponse<List<Address>>
    suspend fun getCart(): ApiResponse<Cart>
}