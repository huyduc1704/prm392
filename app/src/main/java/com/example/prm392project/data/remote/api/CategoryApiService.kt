package com.example.prm392project.data.remote.api

import com.example.prm392project.data.remote.RetrofitInstance
import retrofit2.Response
import retrofit2.http.*


data class CategoryRequest(
    val name: String,
    val description: String? = null,
    val active: Boolean? = true
)

data class CategoryResponse(
    val id: String,
    val name: String,
    val description: String?,
    val active: Boolean,
    val createdAt: String?,
    val updatedAt: String?
)

data class PaginatedResponse<T>(
    val content: List<T>,
    val totalElements: Int,
    val totalPages: Int,
    val number: Int,
    val size: Int
)


interface CategoryApiService {
    @GET("api/v1/categories")
    suspend fun list(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ServerEnvelope<List<CategoryResponse>>> // <- expect array in `data`

    @GET("api/v1/categories/{id}")
    suspend fun get(@Path("id") id: String): Response<ServerEnvelope<CategoryResponse>>

    @POST("api/v1/categories")
    suspend fun create(@Body request: CategoryRequest): Response<ServerEnvelope<CategoryResponse>>

    @PUT("api/v1/categories/{id}")
    suspend fun update(@Path("id") id: String, @Body request: CategoryRequest): Response<ServerEnvelope<CategoryResponse>>

    @DELETE("api/v1/categories/{id}")
    suspend fun delete(@Path("id") id: String): Response<ServerEnvelope<Unit>>
}

val categoryApiService: CategoryApiService
    get() = RetrofitInstance.createService()