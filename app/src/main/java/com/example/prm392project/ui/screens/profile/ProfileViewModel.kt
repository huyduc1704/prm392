package com.example.prm392project.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.UserProfileData
import com.example.prm392project.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {
    var profile by mutableStateOf<UserProfileData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val resp = repo.me() // Response<ServerEnvelope<UserProfileData>>
                if (resp.isSuccessful) {
                    val data = resp.body()?.data
                    if (data != null) {
                        profile = data
                    } else {
                        errorMessage = resp.body()?.message ?: "Empty profile response"
                    }
                } else {
                    val err = try { resp.errorBody()?.string() } catch (_: Exception) { null }
                    errorMessage = err ?: resp.message()
                }
            } catch (t: Throwable) {
                errorMessage = t.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}