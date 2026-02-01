package ru.sicampus.bootcamp2026

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.ui.theme.BlackIcon
import ru.sicampus.bootcamp2026.ui.theme.BluePrimary
import ru.sicampus.bootcamp2026.ui.theme.CustomTypography
import ru.sicampus.bootcamp2026.ui.theme.SineyIney
import ru.sicampus.bootcamp2026.ui.theme.Yellow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val calendarMode = remember { mutableStateOf(CalendarViewMode.DAY) }
    val events = remember { bdEvents() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (calendarMode.value) {
                            CalendarViewMode.DAY -> selectedDate.value.format(
                                DateTimeFormatter.ofPattern("dd MMM yyyy")
                            )
                            CalendarViewMode.WEEK -> "Week of ${selectedDate.value.format(
                                DateTimeFormatter.ofPattern("MMM")
                            )}"
                            CalendarViewMode.MONTH -> selectedDate.value.format(
                                DateTimeFormatter.ofPattern("MMM yyyy")
                            )
                        },
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Режим дня
                        TextButton(
                            onClick = { calendarMode.value = CalendarViewMode.DAY },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (calendarMode.value == CalendarViewMode.DAY) { Yellow } else { BluePrimary },
                                contentColor = BlackIcon
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) { Text( text = "Day", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.displaySmall ) }

                        // Режим недели
                        TextButton(
                            onClick = { calendarMode.value = CalendarViewMode.WEEK },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (calendarMode.value == CalendarViewMode.WEEK) { Yellow } else { BluePrimary },
                                contentColor = BlackIcon
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) { Text( text = "Week", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.displaySmall ) }

                        // Режим месяца
                        TextButton(
                            onClick = { calendarMode.value = CalendarViewMode.MONTH },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (calendarMode.value == CalendarViewMode.MONTH) { Yellow } else { BluePrimary },
                                contentColor = BlackIcon
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) { Text( text = "Month", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.displaySmall ) }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            // стрелки туда сюда
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        when (calendarMode.value) {
                            CalendarViewMode.DAY -> selectedDate.value = selectedDate.value.minusDays(1)
                            CalendarViewMode.WEEK -> selectedDate.value = selectedDate.value.minusWeeks(1)
                            CalendarViewMode.MONTH -> selectedDate.value = selectedDate.value.minusMonths(1)
                        }
                    }
                ) { Icon(Icons.Default.ArrowBack, contentDescription = "Previous") }

                TextButton(
                    onClick = { selectedDate.value = LocalDate.now() }
                ) { Text("Today", style = MaterialTheme.typography.displaySmall) }

                IconButton(
                    onClick = {
                        when (calendarMode.value) {
                            CalendarViewMode.DAY -> selectedDate.value = selectedDate.value.plusDays(1)
                            CalendarViewMode.WEEK -> selectedDate.value = selectedDate.value.plusWeeks(1)
                            CalendarViewMode.MONTH -> selectedDate.value = selectedDate.value.plusMonths(1)
                        }
                    }
                ) { Icon(Icons.Default.ArrowForward, contentDescription = "Next") }
            }

            // переходы по режимам
            when (calendarMode.value) {
                CalendarViewMode.DAY -> DayView(
                    yearMonth = YearMonth.from(selectedDate.value),
                    selectedDate = selectedDate.value,
                    startDate = selectedDate.value.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    events = events.filter { it.date == selectedDate.value }
                )
                CalendarViewMode.WEEK -> WeekView(
                    yearMonth = YearMonth.from(selectedDate.value),
                    selectedDate = selectedDate.value,
                    startDate = selectedDate.value.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                    events = events
                )
                CalendarViewMode.MONTH -> MonthView(
                    yearMonth = YearMonth.from(selectedDate.value),
                    selectedDate = selectedDate.value,
                    events = events,
                    onDateClick = { date ->
                        selectedDate.value = date
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            EventList(
                events = events.filter { it.date == selectedDate.value }
            )
        }
    }
}

@Composable
fun DayView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    startDate: LocalDate,
    events: List<CalendarEvent>,
    onDayClick: (LocalDate) -> Unit = {}
) {
    val days = remember(startDate) { (0..6).map { startDate.plusDays(it.toLong()) } }
    // Заголовок в виде недели
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(modifier = Modifier.width(40.dp))

        days.forEachIndexed { index, date ->
            val isCurrentMonth = date.month == yearMonth.month
            val isToday = date == LocalDate.now()
            val isSelected = date == selectedDate
            val dayEvents = events.filter { it.date == date }

            DayCircle(
                date = date,
                isCurrentMonth = isCurrentMonth ,
                isToday = isToday,
                isSelected = isSelected,
                events = dayEvents,
                onDateClick = { onDayClick(date) },
                modifier = Modifier.weight(1f)
            )
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(10) { index ->

            val hour = 9 + index
            val hourEvents = events.filter { it.startTime.hour == hour }

            Column(
                modifier = Modifier.fillMaxWidth().height(70.dp).border(0.5.dp, Color.LightGray)
            ) {
                // время
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = String.format("%02d:00", hour),
                        modifier = Modifier.width(80.dp).padding(8.dp),
                        fontSize = 14.sp
                    )
                    // встречи
                    Column(modifier = Modifier) {
                        hourEvents.forEach { event ->
                            EventCard(event = event, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeekView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    startDate: LocalDate,
    events: List<CalendarEvent>,
    onDayClick: (LocalDate) -> Unit = {}
) {
    val days = remember(startDate) { (0..6).map { startDate.plusDays(it.toLong()) } }

    // Группируем события по дням и часам
    val eventsGroup = remember(events, days) {
        days.associateWith { day ->
            events.filter { it.date == day }.groupBy { it.startTime.hour }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Заголовок в виде недели
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Box(modifier = Modifier.width(40.dp))

            days.forEachIndexed { index, date ->
                val isCurrentMonth = date.month == yearMonth.month
                val isToday = date == LocalDate.now()
                val isSelected = date == selectedDate
                val dayEvents = events.filter { it.date == date }

                DayCircle(
                    date = date,
                    isCurrentMonth = isCurrentMonth ,
                    isToday = isToday,
                    isSelected = isSelected,
                    events = dayEvents,
                    onDateClick = { onDayClick(date) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(10) { index ->
                HourRow(
                    hour = 9 + index,
                    days = days,
                    eventsGroup = eventsGroup,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun HourRow(
    hour: Int,
    days: List<LocalDate>,
    eventsGroup: Map<LocalDate, Map<Int, List<CalendarEvent>>>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        // Время
        Text(
            text = String.format("%02d:00", hour),
            modifier = Modifier.width(50.dp).padding(horizontal = 8.dp, vertical = 25.dp),
            fontSize = 12.sp
        )

        days.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            ) {
                val hourEvents = eventsGroup[day]?.get(hour) ?: emptyList()

                // Cобытия, которые начинаются в этот час
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp, vertical = 1.dp)
                ) {
                    hourEvents.forEach { event ->
                        EventChip(
                            event = event,
                            modifier = Modifier.fillMaxWidth().height(63.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventChip(
    event: CalendarEvent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors( containerColor = Color(event.color) ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) { Text(text = event.title, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
    }
}

@Composable
fun MonthView(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    events: List<CalendarEvent>,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfCalendar = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val days = remember(yearMonth) { (0 until 42).map { firstDayOfCalendar.plusDays(it.toLong()) } }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DayOfWeek.entries.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.toString().take(3),
                    modifier = Modifier.weight(1f).padding(vertical = 12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(days.chunked(7)) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.forEach { date ->
                        val isCurrentMonth = date.month == yearMonth.month
                        val isToday = date == LocalDate.now()
                        val isSelected = date == selectedDate
                        val dayEvents = events.filter { it.date == date }

                        DayCircle(
                            date = date,
                            isCurrentMonth = isCurrentMonth,
                            isToday = isToday,
                            isSelected = isSelected,
                            events = dayEvents,
                            onDateClick = onDateClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayCircle(
    date: LocalDate,
    isCurrentMonth: Boolean,
    isToday: Boolean,
    isSelected: Boolean,
    events: List<CalendarEvent>,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // кружок
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = when {
                        isSelected -> SineyIney
                        isToday -> BluePrimary
                        else -> Color.Transparent
                    },
                    shape = CircleShape
                )
                .border(
                    width = when {
                        isSelected -> 0.dp
                        isToday -> 2.dp
                        else -> if (isCurrentMonth) 1.dp else 0.5.dp
                    },
                    color = when {
                        isToday -> MaterialTheme.colorScheme.secondary
                        isCurrentMonth -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f) // другой месяц
                    },
                    shape = CircleShape
                )
                .clickable { onDateClick(date) },
            contentAlignment = Alignment.Center
        ) {
            // число кружка
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) // другой месяц
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontSize = 16.sp,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }

        if ( events.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            EventDots(events = events)
        }
    }
}

@Composable
fun EventDots(events: List<CalendarEvent>) {
    val maxDots = 3
    val eventsShow = if (events.size > maxDots) events.take(maxDots) else events
    val eventsNotShow = events.size - maxDots

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        eventsShow.forEach { event ->
            Box(
                modifier = Modifier.size(6.dp).background( color = Color(event.color), shape = CircleShape)
                    .padding(horizontal = 1.dp)
            )
        }

        if (eventsNotShow > 0) {
            Text(
                text = "+$eventsNotShow",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun EventList(events: List<CalendarEvent>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        if (events.isEmpty()) {
            item {
                Text(
                    text = "No events for this day",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
        else {
            items(events) { event ->
                EventCard(event = event, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun EventCard(
    event: CalendarEvent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            // Левая цветная полоса
            Box( modifier = Modifier.width(6.dp).fillMaxHeight().background(Color(event.color)) )

            Spacer( modifier = Modifier.width(8.dp) )

            Column(
                modifier = Modifier.width(100.dp).padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Время начала
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "START",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Время окончания
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "END",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Вертикальный разделитель
            Divider(
                modifier = Modifier.width(1.dp).fillMaxHeight(),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Столбец с названием и описанием
            Column(
                modifier = Modifier.weight(1f).padding(vertical = 12.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (event.description.isNotBlank()) {
                    Text(
                        text = event.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 18.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    MaterialTheme(
        typography = CustomTypography
    ) {
        CalendarScreen()
    }
}

fun bdEvents(): List<CalendarEvent> {
    val today = LocalDate.now()

    return listOf(
        CalendarEvent(
            id = "1",
            title = "Morning Meeting",
            description = "Daily standup",
            date = today,
            startTime = LocalTime.of(9, 30),
            endTime = LocalTime.of(10, 0)
        ),
        CalendarEvent(
            id = "2",
            title = "Lunch",
            description = "",
            date = today,
            startTime = LocalTime.of(13, 0),
            endTime = LocalTime.of(14, 0)
        ),
        CalendarEvent(
            id = "3",
            title = "Gym Session",
            description = "",
            date = today.plusDays(1),
            startTime = LocalTime.of(18, 0),
            endTime = LocalTime.of(19, 30)
        ),
        CalendarEvent(
            id = "4",
            title = "Dentist Appointment",
            description = "Regular checkup",
            date = today.plusDays(3),
            startTime = LocalTime.of(11, 0),
            endTime = LocalTime.of(12, 0)
        )
    )
}