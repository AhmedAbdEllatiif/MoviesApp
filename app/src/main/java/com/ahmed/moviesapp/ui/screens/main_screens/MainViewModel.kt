package com.ahmed.moviesapp.ui.screens.main_screens

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ahmed.moviesapp.data.FireBaseRepository
import com.ahmed.moviesapp.data.MoviesPagingSource.Companion.STARTING_PAGE_INDEX
import com.ahmed.moviesapp.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseRepo: FireBaseRepository
) : ViewModel() {




    private val moviesPageLiveData = MutableLiveData(STARTING_PAGE_INDEX)
    val moviesPerPage = moviesPageLiveData.switchMap{ requiredPage ->
        repository.getMovies(requiredPage).cachedIn(viewModelScope)
    }


    val isUserLoggedIn = firebaseRepo.isUserLoggedIn()





    companion object {
        private const val TAG = "MainViewModel"
    }
}