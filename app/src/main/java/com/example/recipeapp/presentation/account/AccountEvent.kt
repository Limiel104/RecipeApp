package com.example.recipeapp.presentation.account

import com.example.recipeapp.domain.util.RecipeOrder

sealed class AccountEvent {
    data class OnSortRecipes(val recipeOrder: RecipeOrder): AccountEvent()
    object OnLogin: AccountEvent()
    object OnSignup: AccountEvent()
    object OnLogout: AccountEvent()
}