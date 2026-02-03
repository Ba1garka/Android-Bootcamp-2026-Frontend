package ru.sicampus.bootcamp2026.domain.entities

import kotlinx.serialization.SerialName


class EventEntity (
    val title: String,
    val description: String,
    val organizerName: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val participants: List<ParticipantEntity>
)
