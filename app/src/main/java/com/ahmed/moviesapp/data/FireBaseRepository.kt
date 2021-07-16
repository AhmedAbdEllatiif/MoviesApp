package com.ahmed.moviesapp.data


import android.util.Log
import com.ahmed.moviesapp.data.firebaseData.Movie
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.data.firebaseData.User
import com.ahmed.moviesapp.di.FirebaseModule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Named


class FireBaseRepository
@Inject constructor(
    val auth: FirebaseAuth,
) {

    @Inject
    @Named(FirebaseModule.usersReference)
    lateinit var usersRef: DatabaseReference

    @Inject
    @Named(FirebaseModule.navigationReference)
    lateinit var navRef: DatabaseReference



    /**
     * @return true if user is already loggedIn
     * */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /**
     * @return the current user
     * */
    fun currentUser(): FirebaseUser? = auth.currentUser

    /**
     * @return the current user
     * */
    fun currentUserId(): String? {
        val user = currentUser()
        if(user != null){
            return user.uid
        }
        return null
    }

    /**
     * @return DatabaseReference on NavMovie by its id
     * */
    fun referenceOnNavMovie(movieId:String):DatabaseReference?{
        val userId = currentUserId()
        if(userId != null){
            return navRef.child(userId).child(movieId)
        }
       return null
    }

    /**
     * To write new user in database
     * */
    fun writeNewUser(user: User) : Task<Void> =  usersRef.child(user.id).setValue(user)


    /**
     * To write a clicked movie in database
     * */
    fun writeNewClickedMovie(navMovie: NavMovie):Task<Void>  {
        return navRef.child(navMovie.userId)
            .child(navMovie.movieId)
            .setValue(navMovie.movie)
    }


    /**
     * To update visitCount of NavMovie
     * */
    fun update(navMovie: NavMovie){
        val ref = navRef.child(navMovie.userId)
            .child(navMovie.movieId)
        val movie = navMovie.movie
        val moviesPostValue = movie.toMap()
        ref.updateChildren(moviesPostValue)

    }


    /**
     * To logout
     * */
    fun signOut(){
        auth.signOut()
    }

}