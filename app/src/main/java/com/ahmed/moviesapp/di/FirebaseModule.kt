package com.ahmed.moviesapp.di

import com.ahmed.moviesapp.data.repositories.FireBaseRepositoryImpl
import com.ahmed.moviesapp.domain.FireBaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    const val usersReference: String = "UsersReference"
    const val navigationReference: String = "NavigationReference"
    const val userID: String = "userId"

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideCurrentUser(auth: FirebaseAuth): FirebaseUser? = auth.currentUser

    @Provides
    @Singleton
    @Named(userID)
    fun provideUserId(currentUser: FirebaseUser?): String? = currentUser?.uid

    @Provides
    @Singleton
    fun provideIsUserLoggedIn(currentUser: FirebaseUser?): Boolean = currentUser != null

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference = database.reference

    @Provides
    @Singleton
    @Named(usersReference)
    fun provideUsersReference(reference: DatabaseReference): DatabaseReference =
        reference.child("Users")

    @Provides
    @Singleton
    @Named(navigationReference)
    fun provideNavigationReference(reference: DatabaseReference): DatabaseReference =
        reference.child("Navigation")


    @Provides
    @Singleton
    fun provideFireBaseRepository(
        auth: FirebaseAuth,
        @Named(userID) userId: String?,
        @Named(usersReference) usersRef: DatabaseReference,
        @Named(navigationReference) navRef: DatabaseReference
    ): FireBaseRepository = FireBaseRepositoryImpl(auth, userId, usersRef, navRef)


}