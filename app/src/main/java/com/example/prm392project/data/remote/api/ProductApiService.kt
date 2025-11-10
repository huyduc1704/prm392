package com.example.prm392project.data.remote.api

import com.example.prm392project.data.remote.RetrofitInstance
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class VariantResponse(
    val id: String,
    val productId: String,
    val color: String,
    val size: String,
    val price: Double,
    val stockQuantity: Int
)

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val brand: String,
    val imageUrl: String,
    val isActive: Boolean,
    val categoryId: String,
    val variants: List<VariantResponse>
)

interface ProductApiService {
    @GET("api/v1/products/active")
    suspend fun getActiveProducts(): Response<ApiEnvelope<List<ProductResponse>>>

    @GET("api/v1/products/category/{categoryId}")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: String): Response<ApiEnvelope<List<ProductResponse>>>

    @GET("api/v1/products/search")
    suspend fun searchProducts(@Query("name") name: String): Response<ApiEnvelope<List<ProductResponse>>>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ApiEnvelope<ProductResponse>>
}

val productApiService: ProductApiService
    get() = RetrofitInstance.createService()