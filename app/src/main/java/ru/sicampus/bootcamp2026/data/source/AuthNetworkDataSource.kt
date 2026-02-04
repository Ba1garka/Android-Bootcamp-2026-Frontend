package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.RegisterDto
import ru.sicampus.bootcamp2026.data.dto.UserDto


class AuthNetworkDataSource {
    suspend fun checkAuth(token: String?): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("${Network.HOST}/api/auth/login") {
                header(HttpHeaders.Authorization, token)
            }
            result.status == HttpStatusCode.OK
        }.getOrElse { false }
    }

    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<UserDto> = withContext(Dispatchers.IO) { //TODO in own datasourse

        val requestBody = RegisterDto(
            email = email,
            password = password,
            fullName = fullName
        )
        runCatching {
            val result = Network.client.post("${Network.HOST}/api/auth/register"){
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            if (result.status == HttpStatusCode.Conflict){
                val errorBody = result.bodyAsText()
                println("User already exists. Response: $errorBody")
                throw Exception("Пользователь с email $email уже существует")
            }
            result.body<UserDto>()
        }
    }

}