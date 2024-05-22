package com.example.recipeapp.presentation.account

sealed class AccountUiEvent {
    object NavigateToLogin: AccountUiEvent()
    object NavigateToSignup: AccountUiEvent()
}