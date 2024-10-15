package com.example.recipeapp.presentation.account

import com.example.recipeapp.domain.util.RecipeOrder

sealed class AccountEvent {
    data class OnSortRecipes(val recipeOrder: RecipeOrder): AccountEvent()
    data class EnteredName(val name: String): AccountEvent()
    data class EnteredPassword(val password: String): AccountEvent()
    data class EnteredConfirmPassword(val confirmPassword: String): AccountEvent()
    data class OnRecipeSelected(val recipeId: String): AccountEvent()
    object OnLogin: AccountEvent()
    object OnSignup: AccountEvent()
    object OnLogout: AccountEvent()
    object OnEditButtonClicked: AccountEvent()
    object OnSave: AccountEvent()
    object OnDismiss: AccountEvent()
    object OnAddRecipeButtonClicked: AccountEvent()
}