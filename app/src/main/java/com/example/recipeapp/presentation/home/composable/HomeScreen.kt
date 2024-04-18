package com.example.recipeapp.presentation.home.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.recipeapp.presentation.home.HomeEvent
import com.example.recipeapp.presentation.home.HomeUiEvent
import com.example.recipeapp.presentation.home.HomeViewModel
import com.example.recipeapp.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recipes = viewModel.homeState.value.recipes
    val isLoading = viewModel.homeState.value.isLoading
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "HOME_SCREEN_LE")
                when(event) {
                    is HomeUiEvent.NavigateToRecipeDetails -> {
                        navController.navigate(Screen.RecipeDetailsScreen.route)
                    }
                }
            }
        }
    }

    HomeContent(
        recipes = recipes,
        isLoading = isLoading,
        onRecipeSelected = { viewModel.onEvent(HomeEvent.OnRecipeSelected(it)) }
    )
}