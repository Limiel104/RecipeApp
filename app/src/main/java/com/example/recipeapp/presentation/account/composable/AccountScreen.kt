package com.example.recipeapp.presentation.account.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.recipeapp.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    AccountContent (
        scrollBehavior = scrollBehavior,
        onAddRecipe = { navController.navigate(Screen.AddRecipeScreen.route) },
        onRecipeSelected = { navController.navigate(Screen.RecipeDetailsScreen.route) }
    )
}