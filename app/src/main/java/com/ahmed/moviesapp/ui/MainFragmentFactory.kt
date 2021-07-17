package com.ahmed.moviesapp.ui

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.ahmed.moviesapp.ui.screens.main_screens.MovieDetailsFragment
import com.ahmed.moviesapp.ui.screens.main_screens.MoviesListFragment
import com.ahmed.moviesapp.ui.screens.start_app_screens.LoginSignupFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentFactory
@Inject
constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Log.d("FragmentFactory", "instantiate: className >> $className" )
        return when (className) {

            LoginSignupFragment::class.java.name -> {
                LoginSignupFragment()
            }

            MovieDetailsFragment::class.java.name -> {
                MovieDetailsFragment()
            }

            MoviesListFragment::class.java.name -> {
                MoviesListFragment()
            }
            else -> super.instantiate(classLoader, className)
        }

    }
}