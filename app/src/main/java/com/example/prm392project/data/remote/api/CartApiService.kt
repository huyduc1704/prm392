package com.example.prm392project.data.remote.api

import com.example.prm392project.data.remote.RetrofitInstance
import retrofit2.Response
import retrofit2.http.*

data class CartItemRequest(
    val productVariantId: String,
    val quantity: Int
)

data class CartItemUpdateRequest(
    val quantity: Int
)

data class CartItemResponse(
    val id: String,
    val productVariantId: String,
    val productName: String,
    val color: String,
    val size: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

data class CartResponse(
    val id: String,
    val items: List<CartItemResponse>,
    val totalPrice: Double,
    val totalItems: Int
)

interface CartApiService {
    @GET("api/v1/me/cart")
    suspend fun getCart(): Response<ApiEnvelope<CartResponse>>

    @POST("api/v1/me/cart/items")
    suspend fun addItemToCart(@Body item: CartItemRequest): Response<ApiEnvelope<CartResponse>>

    @PATCH("api/v1/me/cart/items/{cartItemId}")
    suspend fun updateCartItem(
        @Path("cartItemId") cartItemId: String,
        @Body item: CartItemUpdateRequest
    ): Response<ApiEnvelope<CartResponse>>

    @DELETE("api/v1/me/cart/items/{cartItemId}")
    suspend fun deleteCartItem(@Path("cartItemId") cartItemId: String): Response<ApiEnvelope<CartResponse>>

    @DELETE("api/v1/me/cart/items")
    suspend fun clearCart(): Response<ApiEnvelope<Unit>>
}

val cartApiService: CartApiService
    get() = RetrofitInstance.createService()
