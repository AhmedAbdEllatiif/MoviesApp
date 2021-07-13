package com.ahmed.moviesapp.data


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FireBaseRepository
@Inject constructor(
     val auth: FirebaseAuth,
) {

    /**
     * @return true if user is already loggedIn
     * */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

}