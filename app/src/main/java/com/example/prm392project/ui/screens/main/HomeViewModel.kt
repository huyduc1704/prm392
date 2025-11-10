package com.example.prm392project.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CategoryResponse
import com.example.prm392project.data.remote.api.ProductResponse
import com.example.prm392project.data.repository.CategoryRepository
import com.example.prm392project.data.repository.DefaultProfileRepository
import com.example.prm392project.data.repository.ProductRepository
import com.example.prm392project.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository = DefaultProfileRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {
    private val _fullName = MutableStateFlow<String>("")
    val fullName: StateFlow<String> = _fullName

    private val _categories = MutableStateFlow<List<CategoryResponse>>(emptyList())
    val categories: StateFlow<List<CategoryResponse>> = _categories

    private val _products = MutableStateFlow<List<ProductResponse>>(emptyList())
    val products: StateFlow<List<ProductResponse>> = _products

    fun fetchUserData() {
        viewModelScope.launch {
            val response = profileRepository.getProfile()
            when (response) {
                is ApiResponse.Loading -> {
                    _fullName.value = "Loading..."
                }
                is ApiResponse.Success -> {
                    _fullName.value = response.data?.username ?: "Unknown"
                }
                is ApiResponse.Error -> {
                    _fullName.value = response.message ?: "Error"
                }
            }
        }
    }

    fun fetchCategories(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            when (val resp = categoryRepository.list(page, size)) {
                is ApiResponse.Success -> {
                    _categories.value = resp.data?.content ?: emptyList()
                }
                is ApiResponse.Error -> {
                    // Handle error
                }
                else -> {
                    // no-op for Loading or unexpected
                }
            }
        }
    }

    fun fetchProductsByCategory(categoryId: String) {
        viewModelScope.launch {
            when (val resp = productRepository.getProductsByCategory(categoryId)) {
                is ApiResponse.Success -> {
                    _products.value = resp.data ?: emptyList()
                }
                is ApiResponse.Error -> {
                    // Handle error
                }
                else -> {
                    // no-op for Loading or unexpected
                }
            }
        }
    }

    fun fetchActiveProducts() {
        viewModelScope.launch {
            when (val resp = productRepository.getActiveProducts()) {
                is ApiResponse.Success -> {
                    _products.value = resp.data ?: emptyList()
                }
                is ApiResponse.Error -> {
                    // Handle error
                }
                else -> {
                    // no-op for Loading or unexpected
                }
            }
        }
    }

    fun searchProducts(name: String) {
        viewModelScope.launch {
            when (val resp = productRepository.searchProducts(name)) {
                is ApiResponse.Success -> {
                    _products.value = resp.data ?: emptyList()
                }
                is ApiResponse.Error -> {
                    // Handle error
                }
                else -> {
                    // no-op for Loading or unexpected
                }
            }
        }
    }
}