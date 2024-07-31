package com.example.recipeapp.presentation.account

sealed class AccountUiEvent {
    data class ShowErrorMessage(val message: String): AccountUiEvent()
    object NavigateToLogin: AccountUiEvent()
    object NavigateToSignup: AccountUiEvent()
}