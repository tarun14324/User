package com.example.userapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.userapp.util.SingleLiveEvent
import com.example.userapp.util.StringUtil.isStrongPassword
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class AuthenticationViewModel : ViewModel() {

    var isRegisterActivity = false

    private val _errorChannel = SingleLiveEvent<String>()
    val errorChannel: SingleLiveEvent<String> get() = _errorChannel

    private val auth = FirebaseAuth.getInstance()

    val loginSuccess = SingleLiveEvent<Boolean>()
    val navigateToScreen = SingleLiveEvent<Boolean>()
    val userName = MutableStateFlow("")
    val password = MutableStateFlow("")

    var usernameError = MutableStateFlow("")
    var passwordError = MutableStateFlow("")

    private fun setUsernameError(errorMessage: String?) {
        usernameError.value = errorMessage ?: ""
    }

    private fun setPasswordError(errorMessage: String?) {
        passwordError.value = errorMessage ?: ""
    }

    fun performValidation() {
        val userNameValue = userName.value
        val passwordValue = password.value

        if (userNameValue.length != 10) {
            setUsernameError("Username is not valid. It should have exactly 10 characters.")
            return
        }
        setUsernameError(null)

        if (passwordValue.length < 7 || !isStrongPassword(passwordValue)) {
            setPasswordError("Password must be at least 7 characters and meet strength criteria")
            return
        }
        setPasswordError(null)

        val userMail = "$userNameValue@gmail.com"

        performAuthAction(userMail, passwordValue)
    }

    private fun performAuthAction(userMail: String, password: String) {
        val authTask = if (isRegisterActivity) {
            auth.createUserWithEmailAndPassword(userMail, password)
        } else {
            auth.signInWithEmailAndPassword(userMail, password)
        }

        authTask.addOnCompleteListener { response ->
            if (response.isSuccessful) {
                val message = if (isRegisterActivity) "Account created successfully" else "Successfully logged in"
                _errorChannel.value = message
                loginSuccess.value = true
            } else {
                _errorChannel.value = response.exception?.message
            }
        }
    }


    fun navigateToReqScreen(){
        navigateToScreen.value=true
    }
}
