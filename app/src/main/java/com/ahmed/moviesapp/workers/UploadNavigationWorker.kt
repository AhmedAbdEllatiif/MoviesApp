package com.ahmed.moviesapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.data.repositories.FireBaseRepository
import com.ahmed.moviesapp.ui.screens.main_screens.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadNavigationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    var firebaseRepo : FireBaseRepository
) : Worker(
    context,
    workerParams
) {

    /* lateinit var auth : FirebaseAuth
     lateinit var firebaseRepo : FireBaseRepository
     val currentUser = auth.currentUser
     val database = FirebaseDatabase.getInstance()
     val navReference = database.reference.child("Navigation")
     val listOfMovies = ArrayList<MovieItem>()*/

    override fun doWork(): Result {
        Log.e(TAG, "doWork: Start" )
        /*val count =
            inputData.getInt("count", 0)*/
        val movies = ArrayList<MovieItem>()
        movies.add(MovieItem(1, "Test", "test", "Test", "test", 8f, "test"))
        movies.add(MovieItem(1, "Test", "test", "Test", "test", 8f, "test"))
        movies.add(MovieItem(1, "Test", "test", "Test", "test", 8f, "test"))
        movies.forEach {
            Log.e(TAG, "doWork: upload ${it.title}" )
              updateOrWriteNavMovie(it)
        }
        //Log.e("UploadNavigationWorker", "doWork: is here with count $count ")

        /* val userId = currentUser!!.uid
         navReference.child(userId)
             .child("384018")
             .setValue(Movie("384018",userId,"Fast & Furious Presents: Hobbs & Shaw",count))*/

        return Result.success()
    }


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

    private fun isNavMovieExistsInDataBase(snapshot: DataSnapshot): Boolean {
        return snapshot.exists()
    }


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


    private fun writeNewClickedMovie(movieItem: MovieItem) {
        val navMovie = prepareNavMovieData(movieItem)
        firebaseRepo.writeNewClickedMovie(navMovie).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "writeNewClickedMovie: Movie add successfully")
            } else {
                val error = if (task.exception == null) null else task.exception
                val errorMessage = if (error == null) "" else "error.message"
                Log.e(TAG, "writeNewClickedMovie: ${errorMessage!!}")
            }

        }
    }


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

    companion object {
        private const val TAG = "UploadNavigationWorker"
    }

}