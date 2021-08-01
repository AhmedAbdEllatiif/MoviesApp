package com.ahmed.moviesapp.data.repositories

import android.util.Log
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.data.firebaseData.User
import com.ahmed.moviesapp.domain.FireBaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FireBaseRepositoryImpl(
    private val auth: FirebaseAuth,
    private val currentUserId: String?,
    private val usersRef: DatabaseReference,
    private val navRef: DatabaseReference,
) : FireBaseRepository {


    /**
     * @return DatabaseReference on NavMovie by its id
     * */
    private fun referenceOnNavMovie(movieId: String): DatabaseReference? {
        if (currentUserId != null) {
            return navRef.child(currentUserId).child(movieId)
        }
        return null
    }


    override fun updateOrWriteNavMovie(navMovie: NavMovie) {
        val navMovieRef = referenceOnNavMovie(navMovie.movieId)
        navMovieRef?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (isNavMovieExistsInDataBase(snapshot))
                    updateMovieVisitCount(navMovie)
                else
                    writeNewClickedMovie(navMovie)


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isNavMovieExistsInDataBase(snapshot: DataSnapshot): Boolean {
        return snapshot.exists()
    }


    private fun updateMovieVisitCount(navMovie: NavMovie) {
        val navMovieRef = referenceOnNavMovie(navMovie.movieId)
        if (navMovieRef != null) {
            navMovieRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val movie = dataSnapshot.getValue(Movie::class.java)
                    if (movie != null) {
                        val updatedCount = movie.count + navMovie.movie.count
                        val moviee =   Movie(
                            movieId = navMovie.movieId,
                            userId = currentUserId!!,
                            count = updatedCount,
                            title = navMovie.movie.title
                        )
                        navMovie.movie = moviee
                        update(navMovie)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } else {
            Log.e(TAG, "updateMovieVisitCount: navMovieRef is null")
        }


    }



    /**
     * To write new user in database
     * */
    override fun writeNewUser(user: User): Task<Void> = usersRef.child(user.id).setValue(user)


    /**
     * To write a clicked movie in database
     * */
    fun writeNewClickedMovie(navMovie: NavMovie): Task<Void> {
        return navRef.child(navMovie.userId)
            .child(navMovie.movieId)
            .setValue(navMovie.movie)
    }


    /**
     * To update visitCount of NavMovie
     * */
    fun update(navMovie: NavMovie) {
        val ref = navRef.child(navMovie.userId)
            .child(navMovie.movieId)
        val movie = navMovie.movie
        val moviesPostValue = movie.toMap()
        ref.updateChildren(moviesPostValue)
    }


    /**
     * To logout
     * */
    override fun signOut() {
        auth.signOut()
    }


    companion object {
        private const val TAG = "FireBaseRepositoryImpl"
    }


}