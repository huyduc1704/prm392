package com.example.prm392project.data.remote

import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T> (val  data: T) : ApiResponse<T>()
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
            val errorText = try {
                errorBody()?.string()
            } catch (e: Exception) {
                null
            }
            ApiResponse.Error(errorText ?: message(), code())
        }
    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "Unknown error")
    }
}