package ru.sicampus.bootcamp2026.ui.screen.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.data.dto.UserDto
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.domain.add.entities.TimeSlot
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity
import ru.sicampus.bootcamp2026.ui.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(onReturnBack: () -> Unit, viewModel: AddViewModel = viewModel()) {
    val user = remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        user.value = AuthLocalDataSource.getCurrentUser()
    }

    val state by viewModel.uiState.collectAsState()

    when(val currentState = state){
        is AddState.Error -> AddErrorState(currentState, onRefresh = { viewModel.getData() })
        is AddState.Loading -> AddLoadingState()
        is AddState.Content -> AddContentState(currentState, user, viewModel, onReturnBack, onRefresh = { viewModel.getData() })
    }

}

@Composable
private  fun AddLoadingState(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private  fun AddErrorState( state: AddState.Error, onRefresh: () -> Unit ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ){
                Text("Refresh")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddContentState(
    state: AddState.Content,
    user: MutableState<UserDto?>,
    viewModel: AddViewModel,
    onReturnBack: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
){
    var meetingTitle by remember { mutableStateOf(TextFieldValue()) }
    var meetingDescription by remember { mutableStateOf(TextFieldValue()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTimeSlot by remember { mutableStateOf<TimeSlot?>(null) }
    val selectedParticipantIds = remember { mutableStateSetOf<Int>() }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimeSlots by remember { mutableStateOf(false) }
    var showParticipantsPicker by remember { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    ) // для кастомного data pickera

    val timeSlots = remember { //TODO получаем из отдельного запроса
        listOf(
            TimeSlot(1, "09:00"),
            TimeSlot(2, "10:00"),
            TimeSlot(3, "11:00"),
            TimeSlot(4, "12:00"),
            TimeSlot(5, "13:00"),
            TimeSlot(6, "14:00"),
            TimeSlot(7, "15:00"),
            TimeSlot(8, "16:00"),
            TimeSlot(9, "17:00"),
            TimeSlot(10, "18:00")
        )
    }

    var participants by remember { mutableStateOf( state.users ) }
    //var participants by remember(state.users) { mutableStateOf( state.users ) }


    val userEntities = remember(participants) {
        participants.filterIsInstance<AddState.Item.User>().map { it.entity }
    }

    val selectedParticipants = remember(userEntities, selectedParticipantIds) {
        userEntities.filter { it.id in selectedParticipantIds }
    }

    val lazyColumnListState = rememberLazyListState()
    val isNeededLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: Int.MIN_VALUE
            val totalItems = lazyColumnListState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 7
        }
    }

    LaunchedEffect(isNeededLoadMore) {
        if(isNeededLoadMore){
            onLoadMore.invoke()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 56.dp)
        ) {
            // заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // стрелка
                IconButton(
                    onClick = { onReturnBack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = "Назад",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "создать встречу",
                    color = Color.Black,
                    style = CustomTypography.displayMedium
                )

                IconButton(
                    onClick = {
                        // TODO: Сохранение встречи в БД
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save),
                        contentDescription = "Сохранить",
                        tint = Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // название встречи
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(30.dp))
                        .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = meetingTitle,   //TODO
                        onValueChange = { meetingTitle = it },
                        textStyle = CustomTypography.labelMedium.copy(
                            color = if (meetingTitle.text.isEmpty()) VeryDarkGrey else Color.Black
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (meetingTitle.text.isEmpty()) {
                                    Text(
                                        text = "название встречи*",
                                        style = CustomTypography.labelMedium,
                                        color = VeryDarkGrey
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Текст ошибки под названием
                if (meetingTitle.text.isEmpty()) {
                    Text(
                        text = "* Заполните название задачи",
                        style = CustomTypography.labelSmall,
                        color = Red,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Описание
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = meetingDescription,  //TODO
                    onValueChange = { meetingDescription = it },
                    textStyle = CustomTypography.labelMedium.copy(
                        color = if (meetingDescription.text.isEmpty()) MediumGray else Color.Black
                    ),
                    maxLines = 5,
                    decorationBox = { innerTextField ->
                        Box {
                            if (meetingDescription.text.isEmpty()) {
                                Text(text = "описание", style = CustomTypography.labelMedium, color = VeryDarkGrey)
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Выбрать дату
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        width = 1.dp,
                        color = MediumGray,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clickable { showDatePicker = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            ?: "установить дату",
                        style = CustomTypography.labelMedium,
                        color = if (selectedDate == null) VeryDarkGrey else Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Выбрать дату",
                        tint = VeryDarkGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Выбрать доступный слот времени
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .clickable { showTimeSlots = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedTimeSlot?.time ?: "выбрать доступный слот",
                        style = CustomTypography.labelMedium,
                        color = if (selectedTimeSlot == null) VeryDarkGrey else Color.Black
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Раскрыть список",
                        tint = VeryDarkGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Добавить участников
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                    .clickable { showParticipantsPicker = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "добавить участников",
                        style = CustomTypography.labelMedium,
                        color = VeryDarkGrey
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Раскрыть список",
                        tint = VeryDarkGrey
                    )
                }
            }

            // выбранные участники
            if (selectedParticipants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "участники",
                        style = CustomTypography.labelMedium,
                        color = Black.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 10.dp, bottom = 12.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = selectedParticipants, key = { it.id }) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(30.dp))
                                    .border(width = 1.dp, color = DarkGray1, shape = RoundedCornerShape(30.dp))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // аватарка
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(MediumGray)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                            contentDescription = "Аватар пользователя",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = item.fullName,
                                        style = CustomTypography.labelMedium,
                                        color = Black
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    IconButton(
                                        onClick = {
                                            selectedParticipantIds.remove(item.id)
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.cross),
                                            contentDescription = "Удалить",
                                            tint = Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // полупрозрачный фон для закрытия dropdown кликом вокруг
        if (showTimeSlots || showParticipantsPicker) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        showTimeSlots = false
                        showParticipantsPicker = false
                    }
            )
        }

        // dropdown для выбора времени
        if (showTimeSlots) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .padding(top = 300.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(30.dp))
                        .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {
                    LazyColumn(
                        modifier = Modifier.height(300.dp)
                    ) {
                        items(timeSlots) { slot ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .clickable {
                                        selectedTimeSlot = slot
                                        showTimeSlots = false
                                    }
                            ) {
                                Text(text = slot.time, style = CustomTypography.labelMedium, color = Black)
                            }
                        }
                    }
                }
            }
        }

        // dropdown для выбора участников
        if (showParticipantsPicker) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .padding(top = 400.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(width = 1.dp, color = MediumGray, shape = RoundedCornerShape(30.dp))
                        .background(Color.White)
                ) {
                    LazyColumn( state = lazyColumnListState) {
                        items(participants) { item ->
                            when(item){
                                is AddState.Item.Error -> ItemError( onRefresh )
                                is AddState.Item.Loading -> ItemLoading()
                                is AddState.Item.User -> ItemUser(item.entity, selectedParticipantIds)
                            }
                        }
                    }
                }
            }
        }

        val datePickerColors = DatePickerDefaults.colors(
            containerColor = SoftWhite, // фон диалога
            titleContentColor = Black, // цвет заголовка
            headlineContentColor = SineyIney, // Цвет выбранной даты
            weekdayContentColor = Black, // цвет дней недели
            subheadContentColor = Black, // цвет месяца/года
            navigationContentColor = SineyIney, // цвет кнопок навигации
            yearContentColor = Black, // цвет года в селекторе
            currentYearContentColor = SineyIney, // цвет текущего года
            selectedYearContentColor = SineyIney, // цвет выбранного года
            dayContentColor = Black, // Цвет дней
            selectedDayContentColor = SoftWhite, // Цвет текста выбранного дня
            selectedDayContainerColor = SineyIney, // Фон выбранного дня
            todayContentColor = SineyIney, // Цвет сегодняшней даты
            todayDateBorderColor = SineyIney // граница сегодняшней даты
        )

        // DatePicker Dialog с кастомными цветами
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                colors = datePickerColors,
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK", color = SineyIney)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Отмена", color = Black)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = datePickerColors
                )
            }
        }
    }
}

@Composable
private  fun ItemError( onRefresh: () -> Unit ){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRefresh
            ){
                Text("Refresh")
            }
        }
    }
}

@Composable
private fun ItemLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}
@Composable
private fun ItemUser(
    user: UserEntity,
    selectedParticipantIds: SnapshotStateSet<Int>
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable {
                // При клике на строку переключаем выбор
                if (selectedParticipantIds.contains(user.id)) {
                    selectedParticipantIds.remove(user.id)
                } else {
                    selectedParticipantIds.add(user.id)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // чекбокс
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(
                    width = 1.dp,
                    color = if (selectedParticipantIds.contains(user.id)) Black else MediumGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(
                    if (selectedParticipantIds.contains(user.id)) Black else Color.White
                )
                .clickable(
                    onClick = {
                        if (selectedParticipantIds.contains(user.id)) {
                            selectedParticipantIds.remove(user.id)
                        } else {
                            selectedParticipantIds.add(user.id)
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selectedParticipantIds.contains(user.id)) {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "Выбрано",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = user.fullName,
            style = CustomTypography.labelMedium,
            color = Black
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CreateMeetingScreenPreview() {
//    AddScreen()
//}