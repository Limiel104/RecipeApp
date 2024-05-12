package com.example.recipeapp.presentation.account

sealed class AccountEvent {
    object OnLogin: AccountEvent()
    object OnSignup: AccountEvent()
}