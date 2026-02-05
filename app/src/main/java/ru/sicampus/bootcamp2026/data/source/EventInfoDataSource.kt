package ru.sicampus.bootcamp2026.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp2026.data.dto.EventDto

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
                error("Status: ${result.status}")
            }
            result.body<List<EventDto>>()
        }
    }
}