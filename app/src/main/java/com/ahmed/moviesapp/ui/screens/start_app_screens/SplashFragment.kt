package com.ahmed.moviesapp.ui.screens.start_app_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ahmed.moviesapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Delay for 1.5sec then navigate to next destination
        CoroutineScope(Main).launch{
            delay(1500)
            findNavController().navigate(R.id.action_splashFragment_to_login_signupFragment)
        }
    }


}