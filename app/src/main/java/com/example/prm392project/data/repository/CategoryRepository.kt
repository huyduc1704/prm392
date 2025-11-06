package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CategoryApiService
import com.example.prm392project.data.remote.api.CategoryRequest
import com.example.prm392project.data.remote.api.CategoryResponse
import com.example.prm392project.data.remote.api.PaginatedResponse
import com.example.prm392project.data.remote.api.categoryApiService
import com.example.prm392project.data.remote.toApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val api: CategoryApiService = categoryApiService) {

    suspend fun list(page: Int = 0, size: Int = 20): ApiResponse<PaginatedResponse<CategoryResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.list(page, size) // now Response<ServerEnvelope<List<CategoryResponse>>>
                if (resp.isSuccessful) {
                    val env = resp.body()
                    val list = env?.data
                    if (list != null) {
                        // map server array into PaginatedResponse so ViewModel keeps working
                        val paginated = PaginatedResponse(
                            content = list,
                            totalElements = list.size,
                            totalPages = 1,
                            number = page,
                            size = size
                        )
                        ApiResponse.Success(paginated)
                    } else {
                        ApiResponse.Error(env?.message ?: "Empty response body", resp.code())
                    }
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    // other methods remain unchanged...
    suspend fun get(id: String): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.get(id)
                if (resp.isSuccessful) {
                    val env = resp.body()
                    if (env?.data != null) ApiResponse.Success(env.data)
                    else ApiResponse.Error(env?.message ?: "Empty response body", resp.code())
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun create(request: CategoryRequest): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.create(request)
                if (resp.isSuccessful) {
                    val env = resp.body()
                    if (env?.data != null) ApiResponse.Success(env.data)
                    else ApiResponse.Error(env?.message ?: "Empty response body", resp.code())
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun update(id: String, request: CategoryRequest): ApiResponse<CategoryResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.update(id, request)
                if (resp.isSuccessful) {
                    val env = resp.body()
                    if (env?.data != null) ApiResponse.Success(env.data)
                    else ApiResponse.Error(env?.message ?: "Empty response body", resp.code())
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun delete(id: String): ApiResponse<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.delete(id)
                if (resp.isSuccessful) {
                    ApiResponse.Success(Unit)
                } else {
                    val err = try { resp.errorBody()?.string() } catch (e: Exception) { null }
                    ApiResponse.Error(err ?: resp.message(), resp.code())
                }
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }
}