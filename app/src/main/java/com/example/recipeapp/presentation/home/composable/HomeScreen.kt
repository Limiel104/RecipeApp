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
    val query = viewModel.homeState.value.query
    val searchSuggestions = viewModel.homeState.value.searchSuggestions
    val categories = viewModel.homeState.value.categories
    val isSearchActive = viewModel.homeState.value.isSearchActive
    val selectedCategory = viewModel.homeState.value.selectedCategory
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
        query = query,
        searchSuggestions = searchSuggestions,
        categories = categories,
        selectedCategory = selectedCategory,
        isSearchActive = isSearchActive,
        isLoading = isLoading,
        onRecipeSelected = { viewModel.onEvent(HomeEvent.OnRecipeSelected(it)) },
        onQueryChange = { viewModel.onEvent(HomeEvent.OnQueryChange(it)) },
        onActiveChange = { viewModel.onEvent(HomeEvent.OnActiveChange) },
        onSearchClicked = { viewModel.onEvent(HomeEvent.OnSearchClicked) },
        onClearClicked = { viewModel.onEvent(HomeEvent.OnClearClicked) },
        onSearchSuggestionClicked = { viewModel.onEvent(HomeEvent.OnSearchSuggestionClicked(it)) },
        onSelectedCategory = { viewModel.onEvent(HomeEvent.OnCategoryClicked(it)) }
    )
}