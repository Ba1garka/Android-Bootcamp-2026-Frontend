package ru.sicampus.bootcamp2026.domain.add.entities

class TimeSlot(
    val id: Int,
    val time: String,
    val isAvailable: Boolean = true
)