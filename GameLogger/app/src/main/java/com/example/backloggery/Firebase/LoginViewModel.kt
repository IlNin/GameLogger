package com.example.backloggery.Firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

// LoginViewModel is used by the other classes to query for whether a user is logged in or not
class LoginViewModel : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData()
        .map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}