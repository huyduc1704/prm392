package com.example.prm392project.data.remote

import com.example.prm392project.data.remote.api.ApiEnvelope
import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String?, val code: Int? = null) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

suspend fun <T> Response<T>.toApiResponse(): ApiResponse<T> {
    return try {
        if (isSuccessful) {
            val body = body()
            if (body != null) ApiResponse.Success(body)
            else ApiResponse.Error("Empty response body", code())
        } else {
            val errorText = try { errorBody()?.string() } catch (_: Exception) { null }
            ApiResponse.Error(errorText ?: message(), code())
        }
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "Unknown error")
    }
}

suspend fun <T> Response<ApiEnvelope<T>>.toDataApiResponse(): ApiResponse<T> {
    return try {
        if (isSuccessful) {
            val env = body()
            val data = env?.data
            if (data != null) ApiResponse.Success(data)
            else ApiResponse.Error(env?.message ?: "Empty data", code())
        } else {
            val errorText = try { errorBody()?.string() } catch (_: Exception) { null }
            ApiResponse.Error(errorText ?: message(), code())
        }
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "Unknown error")
    }
}