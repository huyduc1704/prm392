package com.example.prm392project.ui.screens.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392project.data.remote.ApiResponse
import com.example.prm392project.data.remote.api.CategoryRequest
import com.example.prm392project.data.remote.api.CategoryResponse
import com.example.prm392project.data.remote.api.PaginatedResponse
import com.example.prm392project.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import kotlin.text.set
import kotlin.toString

class CategoryViewModel(
    private val repo: CategoryRepository = CategoryRepository()
) : ViewModel() {
    val categories = mutableStateListOf<CategoryResponse>()
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadCategories()
    }

    fun loadCategories(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                when (val resp = repo.list(page, size)) {
                    is ApiResponse.Success<PaginatedResponse<CategoryResponse>> -> {
                        val content: List<CategoryResponse> = resp.data.content
                        categories.clear()
                        categories.addAll(content)
                    }
                    is ApiResponse.Error -> {
                        errorMessage = resp.message ?: "Failed to load categories"
                    }
                    is ApiResponse.Loading -> {
                        //optional
                    }
                }

            } catch (t: Throwable) {
                errorMessage = t.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
    fun addCategory(request: CategoryRequest, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            val tempId = UUID.randomUUID().toString()
            val newLocal = CategoryResponse(
                id = tempId,
                name = request.name,
                description = request.description,
                active = request.active ?: true,
                createdAt = null,
                updatedAt = null
            )
            categories.add(0, newLocal)

            try {
                when (val resp = repo.create(request)) {
                    is ApiResponse.Success<CategoryResponse> -> {
                        val created = resp.data
                        val idx = categories.indexOfFirst { it.id == tempId }
                        if (idx != -1) categories[idx] = created
                    }
                    is ApiResponse.Error -> {
                        val code = resp.code ?: -1
                        val msg = resp.message ?: ""
                        // Treat empty-body but 2xx as success: reload server list
                        if (msg.contains("Empty response body", ignoreCase = true) || code in 200..299) {
                            // remove optimistic and refresh from server to get canonical data
                            categories.removeAll { it.id == tempId }
                            loadCategories()
                        } else {
                            categories.removeAll { it.id == tempId }
                            errorMessage = msg.ifEmpty { "Failed to create category" }
                        }
                    }
                    is ApiResponse.Loading -> { /* ignore */ }
                }
            } catch (t: Throwable) {
                categories.removeAll { it.id == tempId }
                errorMessage = t.message ?: "Unknown error"
            } finally {
                onDone?.invoke()
            }
        }
    }
    fun updateCategory(id: String, request: CategoryRequest, onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            val idx = categories.indexOfFirst { it.id == id }
            if (idx == -1) {
                onDone?.invoke()
                return@launch
            }
            val old = categories[idx]
            val updatedLocal = old.copy(
                name = request.name,
                description = request.description,
                active = request.active ?: old.active
            )
            categories[idx] = updatedLocal

            try {
                when (val resp = repo.update(id, request)) {
                    is ApiResponse.Success<CategoryResponse> -> {
                        val updated = resp.data
                        categories[idx] = updated
                    }
                    is ApiResponse.Error -> {
                        categories[idx] = old
                        errorMessage = resp.message ?: "Failed to update category"
                    }
                    is ApiResponse.Loading -> { /* ignore */ }
                }
            } catch (t: Throwable) {
                categories[idx] = old
                errorMessage = t.message ?: "Unknown error"
            } finally {
                onDone?.invoke()
            }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            val removed = categories.firstOrNull { it.id == id } ?: return@launch
            categories.removeAll { it.id == id }

            try {
                when (val resp = repo.delete(id)) {
                    is ApiResponse.Success<Unit> -> {
                        // deleted successfully
                    }
                    is ApiResponse.Error -> {
                        categories.add(0, removed)
                        errorMessage = resp.message ?: "Failed to delete category"
                    }
                    is ApiResponse.Loading -> { /* ignore */ }
                }
            } catch (t: Throwable) {
                categories.add(0, removed)
                errorMessage = t.message ?: "Unknown error"
            }
        }
    }
}