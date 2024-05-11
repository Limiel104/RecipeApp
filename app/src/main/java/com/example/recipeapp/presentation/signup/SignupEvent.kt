package com.example.recipeapp.presentation.signup

sealed class SignupEvent {
    data class EnteredEmail(val email: String): SignupEvent()
    data class EnteredPassword(val password: String): SignupEvent()
    data class EnteredConfirmPassword(val confirmPassword: String): SignupEvent()
    data class EnteredName(val name: String): SignupEvent()
    object Signup: SignupEvent()
    object OnGoBack: SignupEvent()
}