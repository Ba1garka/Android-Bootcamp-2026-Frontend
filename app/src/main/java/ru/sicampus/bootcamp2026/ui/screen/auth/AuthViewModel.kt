package ru.sicampus.bootcamp2026.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.AuthRepository
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.auth.CheckAndSaveAuthUseCase
import ru.sicampus.bootcamp2026.domain.auth.CheckAuthFormatUseCase

class AuthViewModel : ViewModel() {
    private val checkAuthFormatUseCase by lazy { CheckAuthFormatUseCase() }
    private val checkAndSaveAuthCodeUseCase by lazy {
        CheckAndSaveAuthUseCase(
            AuthRepository(
                authNetworkDataSourse = AuthNetworkDataSource(),
                authLocalDataSourse = AuthLocalDataSource
            )
        )
    }
    private val _uiState = MutableStateFlow<AuthState>(
        AuthState.Data(
            isEnabledSend = false,
            error = null
        )
    )
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()
    private val _authSuccess = MutableStateFlow(false)
    val authSuccess = _authSuccess.asStateFlow()
    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> {
                viewModelScope.launch {
                    val authCompleted = checkAndSaveAuthCodeUseCase.invoke(intent.login, intent.password)
                    if (authCompleted) {
                        _authSuccess.value = true
                        _uiState.value = AuthState.Data(
                            isEnabledSend = false,
                            error = null
                        )
                    } else {
                        updateStateIfData { oldState ->
                            oldState.copy(
                                error = "Auth not completed"
                            )
                        }
                    }
                }
            }
            is AuthIntent.TextInput -> {
                updateStateIfData { oldState ->
                    oldState.copy(
                        isEnabledSend = checkAuthFormatUseCase.invoke(
                            intent.login,
                            intent.password
                        ),
                        error = null
                    )
                }
            }
        }
    }

    private fun updateStateIfData(lambda: (AuthState.Data) -> AuthState) {
        _uiState.update { state ->
            (state as? AuthState.Data)?.let { lambda.invoke(it) } ?: state
        }

    }

    fun resetAuthSuccess() {
        _authSuccess.value = false
    }
}