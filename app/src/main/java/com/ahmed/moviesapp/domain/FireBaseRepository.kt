package com.ahmed.moviesapp.domain


import android.util.Log
import com.ahmed.moviesapp.data.MovieItem
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


interface FireBaseRepository
 {

    /**
     * To write new user in database
     * */
    fun writeNewUser(user: User) : Task<Void>

   /**
    * To write new movie or update current movie with new visit count
    * */
    fun updateOrWriteNavMovie(navMovie: NavMovie)

    /**
     * To logout
     * */
    fun signOut()

}