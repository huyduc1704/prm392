package com.example.prm392project.data.repository

import com.example.prm392project.data.model.OrderRequest
import com.example.prm392project.data.model.OrderResponse
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.toDataApiResponse
import com.example.prm392project.data.remote.api.OrderApiService
import com.example.prm392project.data.remote.api.ProductApiService
import com.example.prm392project.data.remote.api.ProductResponse
import com.example.prm392project.data.remote.api.orderApiService
import com.example.prm392project.data.remote.api.productApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(
    private val apiProvider: () -> OrderApiService = { orderApiService }
) {
    private val api: OrderApiService by lazy { apiProvider() }

    suspend fun createOrder(orderRequest: OrderRequest): ApiResponse<OrderResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.createOrder(orderRequest).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }
}