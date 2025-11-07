package com.example.prm392project.data.local

import com.example.prm392project.data.remote.AuthStore

class TokenAuthStore : AuthStore {
    private var cachedRole: String? = null

    override fun getToken(): String? = try {
        TokenStore.token
    } catch (t: Throwable) {
        null
    }

    override fun getRole(): String? = cachedRole

    override fun isAuthorized(): Boolean = !getToken().isNullOrBlank()

    override fun saveAuth(token: String, role: String) {
        try {
            TokenStore.token = token
        } catch (_: Throwable) {}
        cachedRole = role
    }

    override fun clear() {
        try {
            TokenStore.token = null
        } catch (_: Throwable) {}
        cachedRole = null
    }
}