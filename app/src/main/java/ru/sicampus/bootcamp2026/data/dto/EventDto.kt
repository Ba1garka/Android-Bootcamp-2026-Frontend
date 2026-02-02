package ru.sicampus.bootcamp2026.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto (
    @SerialName("title")
    val title: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("organizer_id")
    val organizer_id: String?,
    @SerialName("time_slot_id")
    val time_slot_id: String?,
)