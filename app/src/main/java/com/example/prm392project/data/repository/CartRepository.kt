package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.*
import com.example.prm392project.data.remote.toDataApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(
    private val apiProvider: () -> CartApiService = { cartApiService }
) {

    private val api: CartApiService by lazy { apiProvider() }

    suspend fun getCart(): ApiResponse<CartResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getCart().toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun addItemToCart(item: CartItemRequest): ApiResponse<CartResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.addItemToCart(item).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun updateCartItem(cartItemId: String, item: CartItemUpdateRequest): ApiResponse<CartResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.updateCartItem(cartItemId, item).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun deleteCartItem(cartItemId: String): ApiResponse<CartResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.deleteCartItem(cartItemId).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun clearCart(): ApiResponse<Unit> =
        withContext(Dispatchers.IO) {
            try {
                api.clearCart().toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }
}
