package com.example.recipeapp.presentation.add_recipe.composable

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AddRecipeScreen(
    navController: NavController
) {
    val scrollState = rememberScrollState()

    AddRecipeContent(
        scrollState = scrollState
    )
}