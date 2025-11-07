package com.example.prm392project.data.remote.api

import com.example.prm392project.BuildConfig
import com.example.prm392project.data.model.User
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

data class ApiEnvelope<T>(
    val code: Int,
    val success: Boolean,
    val message: String?,
    val data: T?
)

data class LoginData(
    @SerializedName("accessToken") val accessToken: String?,
    val username: String?,
    val email: String?,
    val role: String?
)

private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.PROD_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .build()

val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)

interface AuthApiService {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiEnvelope<Any>>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiEnvelope<LoginData>>
}