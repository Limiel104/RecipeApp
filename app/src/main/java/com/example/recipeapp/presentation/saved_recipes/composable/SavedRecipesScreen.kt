package com.example.recipeapp.presentation.saved_recipes.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.presentation.common.composable.UserNotLoggedInContent
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.saved_recipes.SavedRecipesEvent
import com.example.recipeapp.presentation.saved_recipes.SavedRecipesUiEvent
import com.example.recipeapp.presentation.saved_recipes.SavedRecipesViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SavedRecipesScreen(
    navController: NavController,
    viewModel: SavedRecipesViewModel = hiltViewModel()
) {
    val isUserLoggedIn = viewModel.savedRecipesState.value.isUserLoggedIn
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.savedRecipesUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Saved Recipes Screen LE")
                when (event) {
                    is SavedRecipesUiEvent.NavigateToRecipeDetails -> {
                        navController.navigate(Screen.RecipeDetailsScreen.route + "recipeId=" + event.recipeId)
                    }
                    SavedRecipesUiEvent.NavigateToLogin -> {
                        navController.navigate(Screen.LoginScreen.route + "lastDestination=" + Screen.SavedRecipesScreen.route)
                    }
                    SavedRecipesUiEvent.NavigateToSignup -> {
                        navController.navigate(Screen.SignupScreen.route + "lastDestination=" + Screen.SavedRecipesScreen.route)
                    }
                }
            }
        }
    }

    if(isUserLoggedIn) {
        SavedRecipesContent(
            uiState = viewModel.savedRecipesState.value,
            onRemove = { viewModel.onEvent(SavedRecipesEvent.OnRemove(it)) },
            onRecipeSelected = { viewModel.onEvent(SavedRecipesEvent.OnRecipeSelected(it)) },
            onSortRecipes = { viewModel.onEvent(SavedRecipesEvent.OnSortRecipes(it)) },
            onQueryChange = { viewModel.onEvent(SavedRecipesEvent.OnQueryChange(it)) },
            onActiveChange = { viewModel.onEvent(SavedRecipesEvent.OnActiveChange) },
            onSearchClicked = { viewModel.onEvent(SavedRecipesEvent.OnSearchClicked) },
            onClearClicked = { viewModel.onEvent(SavedRecipesEvent.OnClearClicked) },
            onSearchSuggestionClicked = { viewModel.onEvent(SavedRecipesEvent.OnSearchSuggestionClicked(it)) },
        )
    }
    else {
        UserNotLoggedInContent(
            onLogin = { viewModel.onEvent(SavedRecipesEvent.OnLogin) },
            onSignup = { viewModel.onEvent(SavedRecipesEvent.OnSignup) }
        )
    }
}