package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource

class AuthRepository(
    private val authNetworkDataSourse: AuthNetworkDataSource,
    private  val authLocalDataSourse: AuthLocalDataSource
) {
    suspend fun checkAndAuth(
        login: String,
        password: String,
    ): Boolean {
        authLocalDataSourse.setToken(login,password)
        val result = authNetworkDataSourse.checkAuth(authLocalDataSourse.token ?: return false)
        if(!result) authLocalDataSourse.clearToken()
        return result
    }
}