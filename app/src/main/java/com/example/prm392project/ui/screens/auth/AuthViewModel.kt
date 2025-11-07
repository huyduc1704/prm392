package com.example.prm392project.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.api.ApiEnvelope
import com.example.prm392project.data.remote.api.LoginData
import com.example.prm392project.data.remote.api.LoginRequest
import com.example.prm392project.data.remote.api.RegisterRequest
import com.example.prm392project.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginState = MutableStateFlow<Response<ApiEnvelope<LoginData>>?>(null)
    val loginState: StateFlow<Response<ApiEnvelope<LoginData>>?> = _loginState

    private val _registerState = MutableStateFlow<Response<ApiEnvelope<Any>>?>(null)
    val registerState: StateFlow<Response<ApiEnvelope<Any>>?> = _registerState

    fun register(request: RegisterRequest) {
        viewModelScope.launch { _registerState.value = repository.register(request) }
    }

    fun login(request: LoginRequest) {
        viewModelScope.launch { _loginState.value = repository.login(request) }
    }

    fun clearLoginState() { _loginState.value = null }
    fun clearRegisterState() { _registerState.value = null }
}

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
