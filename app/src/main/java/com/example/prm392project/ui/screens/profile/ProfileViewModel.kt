package com.example.prm392project.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.model.UserProfileData
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.repository.DefaultProfileRepository
import com.example.prm392project.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = DefaultProfileRepository()
) : ViewModel() {

    var profile by mutableStateOf<UserProfileData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val userResp = repository.getProfile()
            val addrResp = repository.getAddresses()

            when {
                userResp is ApiResponse.Success && addrResp is ApiResponse.Success -> {
                    val user = userResp.data
                    profile = UserProfileData(
                        username = user.username,
                        email = user.email,
                        phoneNumber = user.phoneNumber,
                        role = user.role,
                        addresses = addrResp.data
                    )
                }
                userResp is ApiResponse.Error -> {
                    profile = null
                    errorMessage = userResp.message ?: "Failed to load profile"
                }
                addrResp is ApiResponse.Error -> {
                    profile = null
                    errorMessage = addrResp.message ?: "Failed to load addresses"
                }
                else -> {
                    profile = null
                    errorMessage = "Unknown error"
                }
            }

            isLoading = false
        }
    }
}
