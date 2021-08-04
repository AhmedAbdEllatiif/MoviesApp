package com.ahmed.moviesapp.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
    private val syncedNavMovie: MutableLiveData<NavMovie>,
) : FireBaseRepository {


    /**
     * To check if the movie already exists in firebaseDb
     * True >> update visit count
     * False >> write new movie in firebaseDb
     * */
    override fun updateOrWriteNavMovie(navMovie: NavMovie) {
        val navMovieRef = referenceOnMovie(navMovie.movieId)

        navMovieRef?.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (isNavMovieExistsInDataBase(snapshot))
                    updateMovieVisitCount(navMovie)
                else
                    writeNewClickedMovie(navMovie)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "updateOrWriteNavMovie onCancelled: ${databaseError.message}")
            }
        })
    }


    /**
     * To write new user in database
     * */
    override fun writeNewUser(user: User): Task<Void> = usersRef.child(user.id).setValue(user)


    /**
     * To logout
     * */
    override fun signOut() {
        auth.signOut()
    }


    /**
     * @return DatabaseReference on NavMovie by its id
     * */
    private fun referenceOnMovie(movieId: String): DatabaseReference? {
        if (currentUserId != null) {
            return navRef.child(currentUserId).child(movieId)
        }
        return null
    }


    /**
     * @return true if movie already exists in firebaseDb
     * */
    private fun isNavMovieExistsInDataBase(snapshot: DataSnapshot): Boolean {
        return snapshot.exists()
    }


    /**
     * To update visit count
     * */
    private fun updateMovieVisitCount(navMovie: NavMovie) {
        val navMovieRef = referenceOnMovie(navMovie.movieId)
        navMovieRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val movie = dataSnapshot.getValue(Movie::class.java)
                if (movie != null) {

                    val updatedCount = movie.count + navMovie.movie.count

                    navMovie.movie = Movie(
                        movieId = navMovie.movieId,
                        userId = currentUserId!!,
                        count = updatedCount,
                        title = navMovie.movie.title
                    )
                    update(navMovie)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "updateMovieVisitCount onCancelled: ${databaseError.message}")
            }
        })

    }


    /**
     * To write a clicked movie in database
     * */
    fun writeNewClickedMovie(navMovie: NavMovie): Task<Void> {
        return navRef.child(navMovie.userId)
            .child(navMovie.movieId)
            .setValue(navMovie.movie).addOnCompleteListener {
                if (it.isSuccessful) {
                    updateSyncedMovie(navMovie)
                }
            }
    }


    /**
     * To update visitCount of NavMovie
     * */
    fun update(navMovie: NavMovie) {
        val movieRef = navRef.child(navMovie.userId).child(navMovie.movieId)
        val movie = navMovie.movie
        val moviesPostValue = movie.toMap()
        movieRef.updateChildren(moviesPostValue)

        val updateMovieTask = movieRef.updateChildren(moviesPostValue)
        updateMovieTask.addOnCompleteListener {
            if (it.isSuccessful) {
                updateSyncedMovie(navMovie)
            }
        }

    }

    /**
     * To update synced movie
     * */
    private fun updateSyncedMovie(navMovie: NavMovie) {
        syncedNavMovie.value = navMovie
    }


    companion object {
        private const val TAG = "FireBaseRepositoryImpl"
    }


}