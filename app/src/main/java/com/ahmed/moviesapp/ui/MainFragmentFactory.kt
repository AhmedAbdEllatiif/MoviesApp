package com.ahmed.moviesapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.ahmed.moviesapp.ui.screens.start_app_screens.LoginSignupFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainFragmentFactory
@Inject
constructor() : FragmentFactory() {



    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            LoginSignupFragment::class.java.name -> {
                LoginSignupFragment()
            }
            else -> super.instantiate(classLoader, className)

        }

    }
}