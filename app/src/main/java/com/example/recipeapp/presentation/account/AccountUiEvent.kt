package com.example.recipeapp.presentation.account

sealed class AccountUiEvent {
    data class ShowErrorMessage(val message: String): AccountUiEvent()
    data class NavigateToRecipeDetails(val recipeId: String): AccountUiEvent()
    object NavigateToLogin: AccountUiEvent()
    object NavigateToSignup: AccountUiEvent()
    object NavigateToAddRecipe: AccountUiEvent()
}