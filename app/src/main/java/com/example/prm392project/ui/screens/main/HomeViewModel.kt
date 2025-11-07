package com.example.prm392project.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CategoryResponse
import com.example.prm392project.data.repository.CategoryRepository
import com.example.prm392project.data.repository.DefaultProfileRepository
import com.example.prm392project.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository = DefaultProfileRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {
    private val _fullName = MutableStateFlow<String>("")
    val fullName: StateFlow<String> = _fullName

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

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
                    val names = resp.data?.content?.mapNotNull { it.name } ?: emptyList()
                    _categories.value = names.ifEmpty { listOf("All") }
                }
                is ApiResponse.Error -> {
                    if (_categories.value.isEmpty()) _categories.value = listOf("All")
                }
                else -> {
                    // no-op for Loading or unexpected
                }
            }
        }
    }
}