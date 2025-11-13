package com.example.prm392project.ui.screens.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.model.Address
import com.example.prm392project.data.model.OrderRequest
import com.example.prm392project.data.model.OrderResponse
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.repository.DefaultProfileRepository
import com.example.prm392project.data.repository.OrderRepository
import com.example.prm392project.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val profileRepository: ProfileRepository = DefaultProfileRepository()
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders

    // Profile data needed for order
    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses

    var selectedAddressId by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadProfileAndAddresses() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val userResp = profileRepository.getProfile()
            val addrResp = profileRepository.getAddresses()

            when {
                userResp is ApiResponse.Success && addrResp is ApiResponse.Success -> {
                    _userId.value = userResp.data?.id
                    val addrList = addrResp.data ?: emptyList()
                    _addresses.value = addrList
                    if (selectedAddressId == null) {
                        selectedAddressId = addrList.firstOrNull()?.id
                    }
                }
                userResp is ApiResponse.Error -> errorMessage = userResp.message ?: "Failed to load user"
                addrResp is ApiResponse.Error -> errorMessage = addrResp.message ?: "Failed to load addresses"
                else -> errorMessage = "Unknown error"
            }

            isLoading = false
        }
    }

    fun createOrder(request: OrderRequest, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                when (val resp = orderRepository.createOrder(request)) {
                    is ApiResponse.Success<OrderResponse> -> {
                        val created = resp.data
                        if (created != null) _orders.value = _orders.value + created
                    }
                    is ApiResponse.Error -> errorMessage = resp.message ?: "Failed to create order"
                    is ApiResponse.Loading -> Unit
                }
            } catch (t: Throwable) {
                errorMessage = t.message ?: "Unknown error"
            } finally {
                isLoading = false
                onDone?.invoke()
            }
        }
    }

    fun placeOrder(totalPrice: Double, note: String = "", onDone: (() -> Unit)? = null) {
        val uid = _userId.value
        val addrId = selectedAddressId
        if (uid.isNullOrBlank() || addrId.isNullOrBlank()) {
            errorMessage = "Missing user or address"
            onDone?.invoke()
            return
        }
        createOrder(OrderRequest(userId = uid, addressId = addrId, totalPrice = totalPrice, note = note), onDone)
    }
}