package com.example.recipeapp.presentation.login

sealed class LoginUiEvent {
    data class ShowErrorMessage(val message: String): LoginUiEvent()
    object Login: LoginUiEvent()
    object NavigateToSignup: LoginUiEvent()
    object NavigateBack: LoginUiEvent()
}