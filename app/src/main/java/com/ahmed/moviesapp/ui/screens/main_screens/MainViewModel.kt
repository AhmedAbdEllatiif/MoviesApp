package com.ahmed.moviesapp.ui.screens.main_screens

import android.content.Context
import android.util.Log
import androidx.databinding.Observable

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.ahmed.moviesapp.data.MovieDetailsItem
import com.ahmed.moviesapp.domain.FireBaseRepository
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.MoviesPagingSource.Companion.STARTING_PAGE_INDEX
import com.ahmed.moviesapp.domain.Repository
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.domain.RoomRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val firebaseRepo: FireBaseRepository,
    private val roomRepository: RoomRepository,
    private val auth: FirebaseAuth,
    private val workRequest: WorkRequest,
    @ApplicationContext val appContext: Context,
    private val syncedNavMovie: MutableLiveData<NavMovie>,
    val errorData: MutableLiveData<Throwable>
) : ViewModel(), Observable {


    init {
        enqueueUploadNavigationWorker()
        observeSyncedNavMovie()
    }


    // Ui State
    private val _dataUiState: MutableLiveData<MainScreenUIState> =
        MutableLiveData(MainScreenUIState.None)
    val uiUIState: LiveData<MainScreenUIState>
        get() = _dataUiState

    private fun currentUserId():String?{
        if(auth.currentUser != null){
            return auth.currentUser!!.uid
        }
        return null
    }



    private fun observeSyncedNavMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            syncedNavMovie.asFlow().collect {
                if(it!=null){
                    roomRepository.delete(it)
                }
            }
        }
    }

    /**
     * Load movies from paging
     * */
    private val moviesPageLiveData = MutableLiveData(STARTING_PAGE_INDEX)
    val moviesPerPage = moviesPageLiveData.switchMap { requiredPage ->
        repository.getMovies(requiredPage).cachedIn(viewModelScope)
    }

    // MovieItemDataLiveData
    val movieItemLiveData = MutableLiveData(MovieItem())

    // MovieDetailsItemLiveData
    val movieDetails = MutableLiveData(listOf<MovieDetailsItem>())


    /**
     * To enqueue uploadNavigationWorker
     * */
    private fun enqueueUploadNavigationWorker() {
        /**
         * TODO clear roomDB after successful upload
         * */
        val uploadNavigationWorker = workRequest
        WorkManager
            .getInstance(appContext)
            .enqueue(uploadNavigationWorker)

    }


    /**
     * To update the ui
     * */
    fun updateUiState(uiState: MainScreenUIState) {
        _dataUiState.value = uiState
    }


    /**
     * To update the all  movie liveData
     * */
    fun sendMovieData(movieItem: MovieItem) {
        movieItemLiveData.value = movieItem
        updateMovieDetailsData(movieItem)
    }

    /**
     * Prepare a list of MovieDetailsItem then update movieDetails liveData
     * */
    private fun updateMovieDetailsData(movieItem: MovieItem) {
        val overview = MovieDetailsItem(title = "Overview", content = movieItem.plot_synopsis)
        val userRating =
            MovieDetailsItem(title = "Rating", content = movieItem.user_rating.toString())
        val releaseDate = MovieDetailsItem(title = "Release Data", content = movieItem.release_date)
        val items = listOf(
            overview,
            userRating,
            releaseDate
        )
        movieDetails.value = items
    }


    //*************************************************************************************************************\\
    //*                                                                                                             *\\
    //*                                        Handle RoomDB                                                         *\\
    //*                                                                                                               *\\
    //********************************************************************************************************************/
    /**
     * To insert the clicked movie into roomDb
     * */
    fun insertClickedMovieIntoDataBase(movieItem: MovieItem) {
        viewModelScope.launch {
            val navMovie = buildNavMovie(movieItem)
            if (navMovie != null) {
                roomRepository.insertOrUpdate(navMovie)
            }
        }
    }


    /**
     * @return List of saved navMovies from roomDb
     * */
    fun getAllMovies(): Flow<List<NavMovie>> {
        return roomRepository.getAllMovies()
    }

    /**
     * @return NavMovie built from
     * @param movieItem
     * */
    private fun buildNavMovie(movieItem: MovieItem): NavMovie? {
        val userId = currentUserId()
        if (userId != null) {
            val movie = Movie(
                movieId = movieItem.id.toString(),
                title = movieItem.original_title,
                userId = userId
            )
            return NavMovie(
                movieId = movieItem.id.toString(),
                userId = userId,
                movie = movie
            )
        }
        Log.e(TAG, "buildNavMovie: currentUserId is null")
        return null
    }


    //*************************************************************************************************************\\
    //*                                                                                                             *\\
    //*                                        Handle User actions                                                   *\\
    //*                                                                                                               *\\
    //********************************************************************************************************************/
    /**
     * To logout
     * */
    fun logOut() {
        Log.e(TAG, "logOut: ")
        firebaseRepo.signOut()
        updateUiState(MainScreenUIState.LoggedOut)
    }


    //*************************************************************************************************************\\
    //*                                                                                                             *\\
    //*                                        Observable callbacks                                                  *\\
    //*                                                                                                               *\\
    //********************************************************************************************************************/
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    companion object {
        private const val TAG = "MainViewModel"
    }


}