package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CategoryApiService
import com.example.prm392project.data.remote.api.CategoryRequest
import com.example.prm392project.data.remote.api.CategoryResponse
import com.example.prm392project.data.remote.api.PaginatedResponse
import com.example.prm392project.data.remote.api.categoryApiService
import com.example.prm392project.data.remote.toDataApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(
    private val apiProvider: () -> CategoryApiService = { categoryApiService }
) {

    private val api: CategoryApiService by lazy { apiProvider() }

    suspend fun list(page: Int = 0, size: Int = 20): ApiResponse<PaginatedResponse<CategoryResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.list(page, size)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    if (body != null && body.data != null) {
                        val paginated = PaginatedResponse(
                            content = body.data,
                            totalElements = body.data.size,
                            totalPages = 1,
                            number = page,
                            size = size
                        )
                        ApiResponse.Success(paginated)
                    } else {
                        ApiResponse.Error(body?.message ?: "Empty or unexpected response")
                    }
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun get(id: String): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.get(id).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun create(request: CategoryRequest): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.create(request).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun update(id: String, request: CategoryRequest): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.update(id, request).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun delete(id: String): ApiResponse<Unit> =
        withContext(Dispatchers.IO) {
            try {
                api.delete(id).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }
}