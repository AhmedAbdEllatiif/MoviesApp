package com.ahmed.moviesapp.ui.screens.main_screens


sealed class MainScreenUIState {

    object None : MainScreenUIState()

    object Loading : MainScreenUIState()

    object DataLoaded : MainScreenUIState()

    object LoggedOut : MainScreenUIState()

    data class Failed(val error: String="Something went wrong") : MainScreenUIState()
}
