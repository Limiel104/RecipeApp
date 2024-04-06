package com.example.recipeapp.presentation.navigation.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.presentation.account.composable.AccountScreen
import com.example.recipeapp.presentation.add_recipe.composable.AddRecipeScreen
import com.example.recipeapp.presentation.home.composable.HomeScreen
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.recipe_details.composable.RecipeDetailsScreen
import com.example.recipeapp.presentation.saved_recipes.composable.SavedRecipesScreen
import com.example.recipeapp.presentation.shopping_list.composable.ShoppingListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.ShoppingListScreen.route
        ) {
            ShoppingListScreen(navController = navController)
        }

        composable(
            route = Screen.AccountScreen.route
        ) {
            AccountScreen(navController = navController)
        }

        composable(
            route = Screen.RecipeDetailsScreen.route
        ) {
            RecipeDetailsScreen(navController = navController)
        }

        composable(
            route = Screen.AddRecipeScreen.route
        ) {
            AddRecipeScreen(navController = navController)
        }

        composable(
            route = Screen.SavedRecipesScreen.route
        ) {
            SavedRecipesScreen(navController = navController)
        }
    }
}