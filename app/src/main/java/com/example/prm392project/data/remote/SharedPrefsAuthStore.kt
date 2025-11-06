package com.example.prm392project.data.remote

import android.content.Context


interface AuthStore {
    fun getToken(): String?
    fun getRole(): String?
    fun isAuthorized(): Boolean
    fun saveAuth(token: String, role: String)
    fun clear()
}
class SharedPrefsAuthStore(context: Context) : AuthStore {
    private val prefs = context.getSharedPreferences("auth_store", Context.MODE_PRIVATE)
    private val KEY_TOKEN = "key_token"
    private val KEY_ROLE = "key_role"

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    override fun isAuthorized(): Boolean {
        val role = getRole() ?: return false
        return role == "ADMIN" || role == "MODERATOR"
    }

    override fun saveAuth(token: String, role: String) {
        prefs.edit().putString(KEY_TOKEN, token).putString(KEY_ROLE, role).apply()
    }

    override fun clear() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_ROLE).apply()
    }
}