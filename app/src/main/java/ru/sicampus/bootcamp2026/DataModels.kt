package ru.sicampus.bootcamp2026

import androidx.compose.ui.graphics.toArgb
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import java.time.LocalDate
import java.time.LocalTime


data class CalendarEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val color: Int = BluePrimary.toArgb()
)

enum class CalendarViewMode {
    DAY, WEEK, MONTH
}