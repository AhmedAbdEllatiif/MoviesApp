package com.ahmed.moviesapp.ui.screens.main_screens


sealed class LoadingDataState {

    object None : LoadingDataState()

    object Loading : LoadingDataState()

    object DataLoaded : LoadingDataState()

    data class Failed(val error: String="Something went wrong") : LoadingDataState()
}
