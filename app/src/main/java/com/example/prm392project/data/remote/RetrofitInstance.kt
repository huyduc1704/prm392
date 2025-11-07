package com.example.prm392project.data.remote

import com.example.prm392project.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val TIMEOUT_SEC = 60L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private var okHttpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null

    fun init(authStore: AuthStore? = null) {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        authStore?.let { store ->
            builder.addInterceptor { chain ->
                val token = try { store.getToken() } catch (t: Throwable) { null }
                val req = if (!token.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else chain.request()
                chain.proceed(req)
            }
        }

        okHttpClient = builder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.PROD_BASE_URL)
            .client(okHttpClient!!)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    inline fun <reified T> createService(): T {
        checkNotNull(retrofit) { "RetrofitInstance not initialized. Call RetrofitInstance.init(authStore?) first." }
        return retrofit!!.create(T::class.java)
    }

    fun <T> createService(service: Class<T>): T {
        checkNotNull(retrofit) { "RetrofitInstance not initialized. Call RetrofitInstance.init(authStore?) first." }
        return retrofit!!.create(service)
    }
}