package com.ahmed.moviesapp.ui.screens.start_app_screens


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.databinding.FragmentLoginSignupBinding
import com.ahmed.moviesapp.ui.screens.main_screens.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginSignupFragment : Fragment(R.layout.fragment_login_signup) {

    // ViewModel
    private val viewModel: LoginViewModel by viewModels()

    // binding
    private var _binding: FragmentLoginSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // To handle backPressed with changing views
        customOnBackPressedCallBack()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize binding
        _binding = FragmentLoginSignupBinding.bind(view)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel

        // To observe Ui state
        observeUiState()
    }

    /**
     * To observe Ui state
     * */
    private fun observeUiState() {
        viewModel.uiUiState.observe(viewLifecycleOwner, { state ->

            // Hide and Show progress according to loading state
            binding.progressBar.isVisible = state is LoginUiState.Loading

            when (state) {
                is LoginUiState.Failed -> showError(errorMessage = state.error)

                is LoginUiState.None -> {
                    changeViewLoginAndSignup(isLogin = true)
                    Log.d(TAG, "observeUiState: None")
                }

                is LoginUiState.IsLoginScreen -> changeViewLoginAndSignup(isLogin = state.isLogin)

                is LoginUiState.SuccessfulSignUp -> moveToMainScreen()

                is LoginUiState.SuccessfulLogin -> moveToMainScreen()

                else -> Log.d(TAG, "observeUiState: No update for Ui")
            }
        })
    }


    /**
     * To change view from login to signup and visa versa
     * */
    private fun changeViewLoginAndSignup(isLogin: Boolean) {
        binding.RePasswordContainer.isVisible = !isLogin
        binding.apply {
            if (isLogin) {
                btnSubmit.text = getString(R.string.login)
                txtAlreadyHaveAccount.text = getString(R.string.not_have_account)
            } else {
                txtAlreadyHaveAccount.text = getString(R.string.already_have_account)
                btnSubmit.text = getString(R.string.signup)
            }
        }
    }

    /**
     * To handle backPressed with changing views
     * */
    private fun customOnBackPressedCallBack() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if(!viewModel.isLogin){
                        viewModel.changeViewLogin_Signup()
                    }else  requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    /**
     * Navigate to the MainScreens
     * */
    private fun moveToMainScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    /**
     * To show the error message inside toast
     * */
    private fun showError(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val TAG = "SignupFragment"
    }
}