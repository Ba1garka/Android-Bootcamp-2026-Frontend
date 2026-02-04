package ru.sicampus.bootcamp2026.data.source

import kotlinx.io.bytestring.encode
import kotlin.io.encoding.Base64

object AuthLocalDataSource {
    val token: String? get() = _cacheToken
    private var _cacheToken: String? = null

    fun setToken(login: String, password: String){
        val decodePhrase = "$login:$password"
        _cacheToken = "Basic ${Base64.encode(decodePhrase.toByteArray())}"
    }

    fun clearToken(){
        _cacheToken = null
    }
}