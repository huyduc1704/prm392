package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.ProductApiService
import com.example.prm392project.data.remote.api.ProductResponse
import com.example.prm392project.data.remote.api.productApiService
import com.example.prm392project.data.remote.toDataApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val apiProvider: () -> ProductApiService = { productApiService }
) {

    private val api: ProductApiService by lazy { apiProvider() }

    suspend fun getActiveProducts(): ApiResponse<List<ProductResponse>> =
        withContext(Dispatchers.IO) {
            try {
                api.getActiveProducts().toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun getProductsByCategory(categoryId: String): ApiResponse<List<ProductResponse>> =
        withContext(Dispatchers.IO) {
            try {
                api.getProductsByCategory(categoryId).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun searchProducts(name: String): ApiResponse<List<ProductResponse>> =
        withContext(Dispatchers.IO) {
            try {
                api.searchProducts(name).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }

    suspend fun getProductById(id: String): ApiResponse<ProductResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.getProductById(id).toDataApiResponse()
            } catch (t: Throwable) {
                ApiResponse.Error(t.message ?: "Unknown error")
            }
        }
}