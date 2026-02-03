package ru.sicampus.bootcamp2026.domain

import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.domain.entities.EventEntity

class GetEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<List<EventEntity>>{
        return eventRepository.getEvents()
    }
}