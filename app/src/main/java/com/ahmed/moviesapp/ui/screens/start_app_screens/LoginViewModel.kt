package com.ahmed.moviesapp.ui.screens.start_app_screens


import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmed.moviesapp.data.repositories.FireBaseRepository
import com.ahmed.moviesapp.data.firebaseData.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val firebaseRepo: FireBaseRepository
) : ViewModel(), Observable {

    // Bindable
    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPassword = MutableLiveData<String>()

    @Bindable
    val inputRe_Password = MutableLiveData<String>()

    // To make auto login
    val isUserLoggedIn = firebaseRepo.isUserLoggedIn()

    // Used to submit data either as (login or signup)
    var isLogin: Boolean = true


    // Ui State
    private val _dataUiState: MutableLiveData<LoginUiState> = MutableLiveData(LoginUiState.None)
    val uiUiState: LiveData<LoginUiState>
        get() = _dataUiState

    // true if any required fields are missing
    private fun isLoginDataMissing() =
        inputEmail.value.isNullOrBlank() || inputPassword.value.isNullOrBlank()

    // true if any required fields are missing
    private fun isSignupDataMissing() =
        inputEmail.value.isNullOrBlank() || inputPassword.value.isNullOrBlank() || inputRe_Password.value.isNullOrBlank()

    // true if passwords did not match
    private fun isPasswordsNotMatched() = inputPassword.value != inputRe_Password.value


    /**
     * To submit data to firebase
     * */
    fun submitData() {
        // submit data to login
        if (isLogin) {
            if (isLoginDataValid()) {
                updateUiState(LoginUiState.Loading)
                login()
                Log.i(TAG, "Email: ${inputEmail.value} \nPassword: ${inputPassword.value}")
            }
        }

        // submit data to signup
        else {
            if (isSignupDataValid()) {
                updateUiState(LoginUiState.Loading)
                createNewUser()
                Log.i(TAG, "Email: ${inputEmail.value} \nPassword: ${inputPassword.value}")
            }
        }

    }

    /**
     * To make a login process
     * */
    private fun login() {
        val email = inputEmail.value
        val password = inputPassword.value
        firebaseRepo.auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUiState(LoginUiState.SuccessfulLogin)
                } else {
                    // If sign in fails, display a message to the user.
                    val error = if (task.exception == null) null else task.exception
                    val errorMessage = if (error == null) "" else error.message
                    updateUiState(LoginUiState.Failed(error = errorMessage!!))
                }
            }
    }

    /**
     * To create new user in firebase
     * */
    private fun createNewUser() {
        val email = inputEmail.value
        val password = inputPassword.value
        firebaseRepo.auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addNewUserToDataBase()
                } else {
                    val error = if (task.exception == null) null else task.exception
                    val errorMessage = if (error == null) "" else error.message
                    updateUiState(LoginUiState.Failed(error = errorMessage!!))
                }
            }
    }

    /**
     * @return new User
     * */
    private fun preparedUserData(): User? {
        val currentUser = firebaseRepo.currentUser()
        if (currentUser != null) {
            val email = currentUser.email
            val id = currentUser.uid
            return User(id = id, email = email)
        }
        return null
    }

    /**
     * To set the new user value to database
     * */
    private fun addNewUserToDataBase() {
        val user = preparedUserData()
        if (user != null) {
            firebaseRepo.writeNewUser(user).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUiState(LoginUiState.SuccessfulSignUp)
                } else {
                    val error = if (task.exception == null) null else task.exception
                    val errorMessage = if (error == null) "" else error.message
                    updateUiState(LoginUiState.Failed(error = errorMessage!!))
                }
            }
        }
    }


    /**
     * @return true if no required data are missing
     * */
    private fun isLoginDataValid(): Boolean {
        return if (isLoginDataMissing()) {
            updateUiState(LoginUiState.Failed("All Fields are required"))
            false
        } else true
    }


    /**
     * @return if no required data are missing && passwords are matched
     * */
    private fun isSignupDataValid(): Boolean {
        if (isSignupDataMissing()) {
            updateUiState(LoginUiState.Failed("All Fields are required"))
            return false
        } else {
            if (isPasswordsNotMatched()) {
                updateUiState(LoginUiState.Failed("Passwords are not matched"))
                return false
            }
            return true
        }
    }


    /**
     * To change between loginView and SignupView
     * */
    fun changeViewLogin_Signup() {
        isLogin = !isLogin
        updateUiState(LoginUiState.IsLoginScreen(isLogin))
    }


    /**
     * To update the loginUiState
     * To update the ui
     * */
    private fun updateUiState(uiState: LoginUiState) {
        _dataUiState.value = uiState
    }


    // Impl of Observable
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}