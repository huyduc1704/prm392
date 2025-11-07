package com.example.prm392project.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val getToken: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val raw = getToken()
        val headerValue = when {
            raw.isNullOrBlank() -> null
            raw.startsWith("Bearer ", ignoreCase = true) -> raw
            else -> "Bearer $raw"
        }

        val req = chain.request().newBuilder().apply {
            if (headerValue != null) addHeader("Authorization", headerValue)
        }.build()
        return chain.proceed(req)
    }
}
