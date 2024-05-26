package com.example.recipeapp.presentation.add_recipe.composable

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapp.presentation.add_recipe.AddRecipeEvent
import com.example.recipeapp.presentation.add_recipe.AddRecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val modalBottomSheetState = rememberModalBottomSheetState()
    val isServingsBottomSheetOpen = viewModel.addRecipeState.value.isServingsBottomSheetOpened
    val selectedServings = viewModel.addRecipeState.value.selectedServings
    val lastSavedServings = viewModel.addRecipeState.value.lastSavedServings
    val title = viewModel.addRecipeState.value.title
    val titleError = viewModel.addRecipeState.value.titleError
    val description = viewModel.addRecipeState.value.description
    val descriptionError = viewModel.addRecipeState.value.descriptionError
    val ingredient = viewModel.addRecipeState.value.ingredient

    AddRecipeContent(
        scrollState = scrollState,
        modalBottomSheetState = modalBottomSheetState,
        isServingsBottomSheetOpen = isServingsBottomSheetOpen,
        selectedServings = selectedServings,
        lastSavedServings = lastSavedServings,
        title = title,
        titleError = titleError,
        description = description,
        descriptionError = descriptionError,
        ingredient = ingredient,
        ingredientError = "",
        onTitleChange = { viewModel.onEvent(AddRecipeEvent.EnteredTitle(it)) },
        onDescriptionChange = { viewModel.onEvent(AddRecipeEvent.EnteredDescription(it)) },
        onIngredientChange = { viewModel.onEvent(AddRecipeEvent.EnteredIngredient(it)) },
        onSelectedServings = { viewModel.onEvent(AddRecipeEvent.SelectedServings(it)) },
        onServingsPickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed) },
        onServingsPickerSave = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved) },
        onServingsButtonClicked = { viewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked) },
        onAddRecipe = { viewModel.onEvent(AddRecipeEvent.OnAddRecipe) },
    )
}