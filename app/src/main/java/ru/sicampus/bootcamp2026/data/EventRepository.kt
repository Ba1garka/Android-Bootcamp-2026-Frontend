package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.EventInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.home.entities.ParticipantEntity

class EventRepository(
    private val eventInfoDataSource: EventInfoDataSource
) {
    suspend fun getEvents(): Result<List<EventEntity>>{
        return eventInfoDataSource.getEvents().map{ listDto ->
            listDto.mapNotNull { eventDto ->
                EventEntity(
                    title = eventDto.title ?: return@mapNotNull null,
                    description = eventDto.description ?: return@mapNotNull null,
                    organizerName = eventDto.organizerName ?: return@mapNotNull null,
                    date = eventDto.date ?: return@mapNotNull null,
                    startTime = eventDto.startTime ?: return@mapNotNull null,
                    endTime = eventDto.endTime ?: return@mapNotNull null,
                    participants = eventDto.participants?.map { participantDto ->
                        ParticipantEntity(
                            fullName = participantDto.fullName ?: return@mapNotNull null,
                            status = participantDto.status ?: return@mapNotNull null
                        )
                    } ?: emptyList()
                )
            }
        }
    }
}