package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("id")
    val id: Long,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("organizerId")
    val organizerId: Long,

    @SerialName("organizerName")
    val organizerName: String,

    @SerialName("date")
    val date: String,

    @SerialName("startTime")
    val startTime: String,

    @SerialName("endTime")
    val endTime: String,

    @SerialName("participants")
    val participants: List<ParticipantDto>
)