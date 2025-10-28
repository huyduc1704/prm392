package com.example.prm392project.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.AuthResponse
import com.example.prm392project.data.remote.LoginRequest
import com.example.prm392project.data.remote.RegisterRequest
import com.example.prm392project.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<Response<AuthResponse>?>(null)
    val authState: StateFlow<Response<AuthResponse>?> = _authState

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _authState.value = repository.register(request)
        }
    }

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _authState.value = repository.login(request)
        }
    }
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