package com.example.prm392project.data.remote.api

import com.example.prm392project.data.model.OrderRequest
import com.example.prm392project.data.model.OrderResponse
import com.example.prm392project.data.remote.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApiService {
    @POST("api/v1/orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<ApiEnvelope<OrderResponse>>
}

val orderApiService: OrderApiService
    get() = RetrofitInstance.createService()