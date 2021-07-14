package com.ahmed.moviesapp.data


import com.ahmed.moviesapp.data.firebaseData.User
import com.ahmed.moviesapp.di.FirebaseModule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class FireBaseRepository
@Inject constructor(
    val auth: FirebaseAuth,
) {

    @Inject
    @Named(FirebaseModule.usersReference)
    lateinit var usersRef: DatabaseReference


    /**
     * @return true if user is already loggedIn
     * */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /**
     * To return the current user
     * */
    fun currentUser(): FirebaseUser? = auth.currentUser

    /**
     * To write new user in database
     * */
    fun writeNewUser(user: User) : Task<Void> =  usersRef.child(user.id).setValue(user)


}