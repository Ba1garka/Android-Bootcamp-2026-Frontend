package ru.sicampus.bootcamp2026.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.EventRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetEventsUseCase

class HomeViewModel: ViewModel() {
    private val getEventsUseCase = GetEventsUseCase(
        eventRepository = EventRepository(UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(HomeState.Loading)
            getEventsUseCase.invoke().fold(
                onSuccess = { data ->
                    _uiState.emit(HomeState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(HomeState.Error(error.message.orEmpty()))
                }
            )
        }
    }
}