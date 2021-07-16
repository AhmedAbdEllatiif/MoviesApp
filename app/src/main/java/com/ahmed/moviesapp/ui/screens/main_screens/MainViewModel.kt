package com.ahmed.moviesapp.ui.screens.main_screens

import android.util.Log
import androidx.databinding.Observable

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.ahmed.moviesapp.data.MovieDetailsItem
import com.ahmed.moviesapp.data.repositories.FireBaseRepository
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.MoviesPagingSource.Companion.STARTING_PAGE_INDEX
import com.ahmed.moviesapp.data.repositories.Repository
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseRepo: FireBaseRepository
) : ViewModel() , Observable {



    // Ui State
    private val _dataUiState: MutableLiveData<MainScreenUIState> =
        MutableLiveData(MainScreenUIState.None)
    val uiUIState: LiveData<MainScreenUIState>
        get() = _dataUiState


    private val moviesPageLiveData = MutableLiveData(STARTING_PAGE_INDEX)
    val moviesPerPage = moviesPageLiveData.switchMap { requiredPage ->
        repository.getMovies(requiredPage).cachedIn(viewModelScope)
    }

    // MovieItemDataLiveData
    val movieItemLiveData = MutableLiveData(MovieItem())

    // MovieDetailsItemLiveData
    val movieDetails = MutableLiveData(listOf<MovieDetailsItem>())

    val errorData = repository.errorData


    /**
     * To update the all  movie liveData
     * */
    fun sendMovieData(movieItem: MovieItem){
        movieItemLiveData.value = movieItem
        updateMovieDetailsData(movieItem)
    }

    /**
     * Prepare a list of MovieDetailsItem then update movieDetails liveData
     * */
    private fun updateMovieDetailsData(movieItem: MovieItem){
        val overview = MovieDetailsItem(title = "Overview", content = movieItem.plot_synopsis)
        val userRating = MovieDetailsItem(title = "Rating", content = movieItem.user_rating.toString())
        val releaseDate = MovieDetailsItem(title = "Release Data", content = movieItem.release_date)
        val items = listOf(
            overview,
            userRating,
            releaseDate
        )
        movieDetails.value = items
    }

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
                    title = movieItem.original_title
                )
            else
                Movie(
                    movieId = movieItem.id.toString(),
                    userId = userId,
                    title = movieItem.original_title
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
    fun updateUiState(uiState: MainScreenUIState) {
        _dataUiState.value = uiState
    }


    /**
     * To logout
     * */
     fun logOut(){
        Log.e(TAG, "logOut: " )
        firebaseRepo.signOut()
        updateUiState(MainScreenUIState.LoggedOut)
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}