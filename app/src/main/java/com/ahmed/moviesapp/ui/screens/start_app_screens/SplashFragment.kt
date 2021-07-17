package com.ahmed.moviesapp.ui.screens.start_app_screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.ui.screens.main_screens.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashFragment : Fragment(R.layout.fragment_splash) {

    // ViewModel
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(isUserLoggedOut()){
            navigateToLoginSignupScreen()
        }else{

            // Delay for 1.5sec then navigate to next destination
            CoroutineScope(Main).launch{
                delay(1500)
                if (isUserLoggedIn()){
                    navigateToMainActivity()
                }else{
                    navigateToLoginSignupScreen()
                }
            }


        }


    }


    /**
     * @return true if user logged out and return to login screen
     * */
    private fun isUserLoggedOut() :Boolean{
        return requireActivity().intent.hasExtra("isLoggedOut")
    }

    /**
     * @return true if user loggedIn
     * */
    private fun isUserLoggedIn():Boolean{
       return viewModel.isUserLoggedIn
    }

    /**
     * The user is already logged in
     * Then move to MainActivity
     * */
    private fun navigateToMainActivity(){
        val intent = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    /**
     * The user is not logged in
     * Then move to LoginSignupFragment
     * */
    private fun navigateToLoginSignupScreen(){
        findNavController().navigate(R.id.action_splashFragment_to_login_signupFragment)
    }


}