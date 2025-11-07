package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.api.AuthApiService
import com.example.prm392project.data.remote.api.LoginRequest
import com.example.prm392project.data.remote.api.RegisterRequest

class AuthRepository(private val api: AuthApiService) {
    suspend fun register(request: RegisterRequest) = api.register(request)
    suspend fun login(request: LoginRequest) = api.login(request)
}