package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.EventDto
import ru.sicampus.bootcamp2026.data.dto.RegisterDto
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.domain.home.entities.InvitationEntity

class EventInfoDataSource {
    suspend fun getEvents(): Result<List<EventDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val currentUser = AuthLocalDataSource.getCurrentUser()

            if (currentUser == null || currentUser.id == null) {
                throw Exception("Пользователь не авторизован")
            }

            val userId = currentUser.id

            val result = Network.client.get("${Network.HOST}/api/meetings/user/$userId"){
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK){
                error("Статус: ${result.status}")
            }
            result.body<List<EventDto>>()
        }
    }

    suspend fun getInvitations(): Result<List<EventDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val currentUser = AuthLocalDataSource.getCurrentUser()

            if (currentUser == null || currentUser.id == null) {
                throw Exception("Пользователь не авторизован")
            }

            val userId = currentUser.id

            val result = Network.client.get("${Network.HOST}/api/invitations/$userId"){
                addAuthHeader()
            }
            if (result.status != HttpStatusCode.OK){
                error("Статус: ${result.status}")
            }
            result.body<List<EventDto>>()
        }
    }

    suspend fun changeInvitations(
        meetingId: Int,
        userId: Int,
        response: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val requestBody = InvitationEntity(
                meetingId = meetingId,
                userId = userId,
                response = response
            )

            val result = Network.client.put("${Network.HOST}/api/invitations") {
                addAuthHeader()
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            result.body<Unit>()
        }
    }

}