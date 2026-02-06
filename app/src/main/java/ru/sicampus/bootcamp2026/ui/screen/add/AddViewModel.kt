package ru.sicampus.bootcamp2026.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.add.GetUsersUseCase

class AddViewModel: ViewModel() {
    private val getUsersUseCase = GetUsersUseCase(
        userRepository = UserRepository( AuthNetworkDataSource(), UserInfoDataSource())
    )
    private val _uiState: MutableStateFlow<AddState> = MutableStateFlow(AddState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            _uiState.emit(AddState.Loading)
            getUsersUseCase.invoke(0).fold(
                onSuccess = { data ->
                    _uiState.emit(AddState.Content(data))
                },
                onFailure = { error ->
                    _uiState.emit(AddState.Error(error.message.orEmpty()))
                }
            )
        }
    }
}