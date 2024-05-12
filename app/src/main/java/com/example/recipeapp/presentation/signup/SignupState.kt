package com.example.recipeapp.presentation.signup

data class SignupState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val lastDestination: String = "",
    val isLoading: Boolean = false
)