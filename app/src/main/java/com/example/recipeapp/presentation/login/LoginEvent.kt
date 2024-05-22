package com.example.recipeapp.presentation.login

sealed class LoginEvent {
    data class EnteredEmail(val email: String): LoginEvent()
    data class EnteredPassword(val password: String): LoginEvent()
    object OnLogin: LoginEvent()
    object OnSignup: LoginEvent()
    object OnGoBack: LoginEvent()
}