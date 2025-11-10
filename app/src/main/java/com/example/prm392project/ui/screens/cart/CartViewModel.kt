package com.example.prm392project.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CartItemRequest
import com.example.prm392project.data.remote.api.CartItemUpdateRequest
import com.example.prm392project.data.remote.api.CartResponse
import com.example.prm392project.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {

    private val _cart = MutableStateFlow<CartResponse?>(null)
    val cart: StateFlow<CartResponse?> = _cart

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getCart() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = cartRepository.getCart()) {
                is ApiResponse.Success -> {
                    _cart.value = response.data
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

    fun addItemToCart(item: CartItemRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = cartRepository.addItemToCart(item)) {
                is ApiResponse.Success -> {
                    _cart.value = response.data
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

    fun updateCartItem(cartItemId: String, item: CartItemUpdateRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = cartRepository.updateCartItem(cartItemId, item)) {
                is ApiResponse.Success -> {
                    _cart.value = response.data
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

    fun deleteCartItem(cartItemId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = cartRepository.deleteCartItem(cartItemId)) {
                is ApiResponse.Success -> {
                    _cart.value = response.data
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

    fun clearCart() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            when (val response = cartRepository.clearCart()) {
                is ApiResponse.Success -> {
                    getCart() // Refresh cart after clearing
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
