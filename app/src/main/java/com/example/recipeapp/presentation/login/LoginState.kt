package com.example.recipeapp.presentation.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val lastDestination: String = "",
    val isLoading: Boolean = false
)