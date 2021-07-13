package com.ahmed.moviesapp.ui.screens.start_app_screens

sealed class LoginUiState {

    object None : LoginUiState()

    object Loading : LoginUiState()

    object SuccessfulSignUp : LoginUiState()

    object SuccessfulLogin : LoginUiState()

    class IsLoginScreen(val isLogin: Boolean = true) : LoginUiState()

    data class Failed(val error: String="Something went wrong") : LoginUiState()
}
