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
    val isPrepTimeBottomSheetOpen = viewModel.addRecipeState.value.isPrepTimeBottomSheetOpened
    val selectedPrepTimeMinutes = viewModel.addRecipeState.value.selectedPrepTimeMinutes
    val selectedPrepTimeHours = viewModel.addRecipeState.value.selectedPrepTimeHours
    val lastSavedPrepTime = viewModel.addRecipeState.value.lastSavedPrepTime
    val title = viewModel.addRecipeState.value.title
    val titleError = viewModel.addRecipeState.value.titleError
    val description = viewModel.addRecipeState.value.description
    val descriptionError = viewModel.addRecipeState.value.descriptionError
    val ingredient = viewModel.addRecipeState.value.ingredient
    val ingredients = viewModel.addRecipeState.value.ingredients
    val isDropDownMenuExpanded = viewModel.addRecipeState.value.isDropDownMenuExpanded
    val recipeIngredients = viewModel.addRecipeState.value.recipeIngredients

    AddRecipeContent(
        scrollState = scrollState,
        modalBottomSheetState = modalBottomSheetState,
        isServingsBottomSheetOpen = isServingsBottomSheetOpen,
        selectedServings = selectedServings,
        lastSavedServings = lastSavedServings,
        isPrepTimeBottomSheetOpen = isPrepTimeBottomSheetOpen,
        selectedPrepTimeHours = selectedPrepTimeHours,
        selectedPrepTimeMinutes = selectedPrepTimeMinutes,
        lastSavedPrepTime = lastSavedPrepTime,
        title = title,
        titleError = titleError,
        description = description,
        descriptionError = descriptionError,
        ingredient = ingredient,
        ingredients = ingredients,
        isDropDownMenuExpanded = isDropDownMenuExpanded,
        onTitleChange = { viewModel.onEvent(AddRecipeEvent.EnteredTitle(it)) },
        onDescriptionChange = { viewModel.onEvent(AddRecipeEvent.EnteredDescription(it)) },
        onIngredientChange = { viewModel.onEvent(AddRecipeEvent.EnteredIngredient(it)) },
        onSelectedServings = { viewModel.onEvent(AddRecipeEvent.SelectedServings(it)) },
        onServingsPickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed) },
        onServingsPickerSave = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved) },
        onServingsButtonClicked = { viewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked) },
        onSelectedPrepTimeHours = { viewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours(it)) },
        onSelectedPrepTimeMinutes = { viewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes(it)) },
        onPrepTimePickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed) },
        onPrepTimePickerSave = { viewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved) },
        onPrepTimeButtonClicked = { viewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked) },
        onExpandedChange = { viewModel.onEvent(AddRecipeEvent.OnExpandChange) },
        onIngredientSuggestionClick = {viewModel.onEvent(AddRecipeEvent.SelectedIngredient(it))},
        onAddRecipe = { viewModel.onEvent(AddRecipeEvent.OnAddRecipe) },
    )
}