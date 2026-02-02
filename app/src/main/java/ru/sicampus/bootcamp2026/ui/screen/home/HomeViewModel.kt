package ru.sicampus.bootcamp2026.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(HomeState.Loading)
            delay(2000L)
            _uiState.emit(HomeState.Error("User error"))
        }
    }
}