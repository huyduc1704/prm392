package com.example.prm392project.data.remote

import com.example.prm392project.data.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val role: String = "CUSTOMER",
    val addresses: List<AddressRequest>
)

data class AddressRequest(
    val fullName: String,
    val phoneNumber: String,
    val city: String,
    val ward: String,
    val street: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val user: User,
    val token: String
)
val retrofit = Retrofit.Builder()
    .baseUrl("http://34.63.203.101:8080/") // Replace with your API base URL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val authApiService = retrofit.create(AuthApiService::class.java)

interface AuthApiService {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}
