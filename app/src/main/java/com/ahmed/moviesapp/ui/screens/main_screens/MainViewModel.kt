package com.ahmed.moviesapp.ui.screens.main_screens

import android.util.Log

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmed.moviesapp.data.FireBaseRepository
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.MoviesPagingSource.Companion.STARTING_PAGE_INDEX
import com.ahmed.moviesapp.data.Repository
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseRepo: FireBaseRepository
) : ViewModel() {



    // Ui State
    private val _dataUiState: MutableLiveData<LoadingDataState> =
        MutableLiveData(LoadingDataState.None)
    val uiUIState: LiveData<LoadingDataState>
        get() = _dataUiState


    private val moviesPageLiveData = MutableLiveData(STARTING_PAGE_INDEX)
    val moviesPerPage = moviesPageLiveData.switchMap { requiredPage ->
        repository.getMovies(requiredPage).cachedIn(viewModelScope)
    }

    // MovieItemDataLiveData
    val movieItemLiveData = MutableLiveData(MovieItem())

    val errorData = repository.errorData

    /**
     * Check if the movie already exists in the database
     * Exists -> increase the current visitCount by 1
     * NotExists -> add the movie in the database
     * */
    fun updateOrWriteNavMovie(movieItem: MovieItem) {
        val navMovieRef = firebaseRepo.referenceOnNavMovie(movieItem.id.toString())
        navMovieRef?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (isNavMovieExistsInDataBase(snapshot))
                    updateMovieVisitCount(movieItem)
                else
                    writeNewClickedMovie(movieItem)


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    /**
     * @return true if the snapshot is exists
     * That means the movie is already in the database
     * */
    private fun isNavMovieExistsInDataBase(snapshot: DataSnapshot): Boolean {
        return snapshot.exists()
    }

    /**
     * @return  NavMovie to be added to database
     * */
    private fun prepareNavMovieData(movieItem: MovieItem, updatedCount: Int = 0): NavMovie {
        val userId = firebaseRepo.currentUserId()!!
        val movie =
            if (updatedCount > 0)
                Movie(
                    movieId = movieItem.id.toString(),
                    userId = userId,
                    count = updatedCount,
                    title = movieItem.original_title!!
                )
            else
                Movie(
                    movieId = movieItem.id.toString(),
                    userId = userId,
                    title = movieItem.original_title!!
                )


        return NavMovie(
            movieId = movieItem.id.toString(),
            userId = userId,
            movie = movie
        )

    }

    /**
     * Update visit count to a NavMovie that already exists in the database
     * */
    private fun updateMovieVisitCount(movieItem: MovieItem) {
        val navMovieRef = firebaseRepo.referenceOnNavMovie(movieItem.id.toString())
        if (navMovieRef != null) {
            navMovieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val movie = dataSnapshot.getValue(Movie::class.java)
                    if (movie != null) {
                        val updatedCount = movie.count + 1
                        val updatedNavMovie = prepareNavMovieData(movieItem, updatedCount)
                        firebaseRepo.update(updatedNavMovie)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } else {
            Log.e(TAG, "updateMovieVisitCount: navMovieRef is null")
        }


    }

    /**
     * Write new NavMovie in the database
     * */
    private fun writeNewClickedMovie(movieItem: MovieItem) {
        val navMovie = prepareNavMovieData(movieItem)
        firebaseRepo.writeNewClickedMovie(navMovie).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "writeNewClickedMovie: Movie add successfully")
            } else {
                val error = if (task.exception == null) null else task.exception
                val errorMessage = if (error == null) "" else error.message
                Log.e(TAG, "writeNewClickedMovie: ${errorMessage!!}")
            }

        }
    }


    /**
     * To update the ui
     * */
    fun updateUiState(uiState: LoadingDataState) {
        _dataUiState.value = uiState
    }

    val isUserLoggedIn = firebaseRepo.isUserLoggedIn()


    companion object {
        private const val TAG = "MainViewModel"
    }
}