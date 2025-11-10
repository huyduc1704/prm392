package com.example.prm392project.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.ProductResponse
import com.example.prm392project.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _product = MutableStateFlow<ProductResponse?>(null)
    val product: StateFlow<ProductResponse?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchProductDetail(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = productRepository.getProductById(productId)) {
                is ApiResponse.Success -> {
                    _product.value = response.data
                }
                is ApiResponse.Error -> {
                    _error.value = response.message
                }
                is ApiResponse.Loading -> {
                    // No-op
                }
            }
            _isLoading.value = false
        }
    }
}
