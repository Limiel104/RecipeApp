package com.example.recipeapp.domain.util

sealed class RecipeOrder {
    object DateAscending : RecipeOrder()
    object DateDescending : RecipeOrder()
}