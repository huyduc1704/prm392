package com.example.prm392project.data.remote.api

import com.example.prm392project.BuildConfig
import com.example.prm392project.data.local.TokenStore
import com.example.prm392project.data.model.Address
import com.example.prm392project.data.model.Cart
import com.example.prm392project.data.model.User
import com.example.prm392project.data.remote.AuthorizationInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/v1/me")
    suspend fun getMe(): Response<ApiEnvelope<User>>

    @GET("api/v1/me/addresses")
    suspend fun getMyAddresses(): Response<ApiEnvelope<List<Address>>>

    @GET("api/v1/me/cart")
    suspend fun getMyCart(): Response<ApiEnvelope<Cart>>
}

private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(AuthorizationInterceptor { TokenStore.token })
    .addInterceptor(logging)
    .build()

val profileRetrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.PROD_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .build()

val profileApiService: ProfileApiService = profileRetrofit.create(ProfileApiService::class.java)