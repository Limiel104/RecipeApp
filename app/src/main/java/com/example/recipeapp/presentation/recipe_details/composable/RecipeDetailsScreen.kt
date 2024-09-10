package com.example.recipeapp.presentation.recipe_details.composable

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsEvent
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsViewModel

@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    RecipeDetailsContent(
        scrollState = scrollState,
        uiState = viewModel.recipeDetailsState.value,
        onTabChanged = { viewModel.onEvent(RecipeDetailsEvent.OnTabChanged(it)) }
    )
}