package com.example.recipeapp.presentation.signup

sealed class SignupUiEvent {
    data class ShowErrorMessage(val message: String): SignupUiEvent()
    object Signup: SignupUiEvent()
    object NavigateBack: SignupUiEvent()
}