package com.example.recipeapp.presentation.recipe_details.composable

import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsEvent
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsUiEvent
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecipeDetailsScreen(
    navController: NavController,
    viewModel: RecipeDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.recipeDetailsUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "RecipeDetails Screen LE")
                when (event) {
                    RecipeDetailsUiEvent.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }

    RecipeDetailsContent(
        scrollState = scrollState,
        uiState = viewModel.recipeDetailsState.value,
        onTabChanged = { viewModel.onEvent(RecipeDetailsEvent.OnTabChanged(it)) },
        onLessServings = { viewModel.onEvent(RecipeDetailsEvent.OnLessServings) },
        onMoreServings = { viewModel.onEvent(RecipeDetailsEvent.OnMoreServings) },
        onSaveRecipe = { viewModel.onEvent(RecipeDetailsEvent.OnSaveRecipe) },
        onGoBack = { viewModel.onEvent(RecipeDetailsEvent.OnGoBack) }
    )
}