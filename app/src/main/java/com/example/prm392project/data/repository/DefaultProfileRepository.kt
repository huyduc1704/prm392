package com.example.prm392project.data.repository

import com.example.prm392project.data.model.Address
import com.example.prm392project.data.model.Cart
import com.example.prm392project.data.model.User
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.ProfileApiService
import com.example.prm392project.data.remote.api.profileApiService
import com.example.prm392project.data.remote.toDataApiResponse

class DefaultProfileRepository(
    private val api: ProfileApiService = profileApiService
) : ProfileRepository {

    override suspend fun getProfile(): ApiResponse<User> {
        return try {
            api.getMe().toDataApiResponse()
        } catch (e: Exception) {
            ApiResponse.Error(e.message)
        }
    }

    override suspend fun getAddresses(): ApiResponse<List<Address>> {
        return try {
            api.getMyAddresses().toDataApiResponse()
        } catch (e: Exception) {
            ApiResponse.Error(e.message)
        }
    }

    override suspend fun getCart(): ApiResponse<Cart> {
        return try {
            api.getMyCart().toDataApiResponse()
        } catch (t: Throwable) {
            ApiResponse.Error(t.message ?: "Network error")
        }
    }
}