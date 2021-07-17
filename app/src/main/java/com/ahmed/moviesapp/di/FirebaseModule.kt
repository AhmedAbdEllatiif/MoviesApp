package com.ahmed.moviesapp.di

import com.google.firebase.auth.FirebaseAuth
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

    const val usersReference:String = "UsersReference"
    const val navigationReference:String = "NavigationReference"

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference = database.reference

    @Provides
    @Singleton
    @Named(usersReference)
    fun provideUsersReference(reference: DatabaseReference): DatabaseReference = reference.child("Users")

    @Provides
    @Singleton
    @Named(navigationReference)
    fun provideNavigationReference(reference: DatabaseReference): DatabaseReference = reference.child("Navigation")
}