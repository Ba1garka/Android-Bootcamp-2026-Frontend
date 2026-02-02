package ru.sicampus.bootcamp2026.ui.screen.home

sealed interface HomeState {
    data class Error( val reason: String ): HomeState
    data object Loading: HomeState
    data class Content(
        val events: List<Any>
    ): HomeState
}