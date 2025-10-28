package com.example.prm392project.data.repository

import com.example.prm392project.data.remote.AuthApiService
import com.example.prm392project.data.remote.LoginRequest
import com.example.prm392project.data.remote.RegisterRequest

class AuthRepository(private val api: AuthApiService) {
    suspend fun register(request: RegisterRequest) = api.register(request)
    suspend fun login(request: LoginRequest) = api.login(request)
}