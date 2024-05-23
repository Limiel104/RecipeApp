package com.example.recipeapp.presentation.add_recipe.composable

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapp.presentation.add_recipe.AddRecipeEvent
import com.example.recipeapp.presentation.add_recipe.AddRecipeViewModel

@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val title = viewModel.addRecipeState.value.title
    val titleError = viewModel.addRecipeState.value.titleError
    val description = viewModel.addRecipeState.value.description
    val descriptionError = viewModel.addRecipeState.value.descriptionError
    val ingredient = viewModel.addRecipeState.value.ingredient

    AddRecipeContent(
        scrollState = scrollState,
        title = title,
        titleError = titleError,
        description = description,
        descriptionError = descriptionError,
        ingredient = ingredient,
        ingredientError = "",
        onTitleChange = { viewModel.onEvent(AddRecipeEvent.EnteredTitle(it)) },
        onDescriptionChange = { viewModel.onEvent(AddRecipeEvent.EnteredDescription(it)) },
        onIngredientChange = { viewModel.onEvent(AddRecipeEvent.EnteredIngredient(it)) },
        onAddRecipe = { viewModel.onEvent(AddRecipeEvent.OnAddRecipe) }
    )
}