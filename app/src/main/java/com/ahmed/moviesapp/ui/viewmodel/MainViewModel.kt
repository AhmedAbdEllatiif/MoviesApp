package com.ahmed.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.ahmed.moviesapp.data.MoviesPage
import com.ahmed.moviesapp.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {


    private val moviesLiveData = MutableLiveData<MoviesPage>()
    val movies: LiveData<MoviesPage> = moviesLiveData
    init {
        Log.d(TAG, " init start  ")
        viewModelScope.launch{
            val movies = repository.getMovies()
            delay(5000)
            moviesLiveData.value = movies

        }
    }




    companion object {
        private const val TAG = "MainViewModel"
    }
}