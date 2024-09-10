package com.example.recipeapp.presentation.navigation.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipeapp.presentation.account.composable.AccountScreen
import com.example.recipeapp.presentation.add_recipe.composable.AddRecipeScreen
import com.example.recipeapp.presentation.home.composable.HomeScreen
import com.example.recipeapp.presentation.login.composable.LoginScreen
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.recipe_details.composable.RecipeDetailsScreen
import com.example.recipeapp.presentation.saved_recipes.composable.SavedRecipesScreen
import com.example.recipeapp.presentation.shopping_list.composable.ShoppingListScreen
import com.example.recipeapp.presentation.signup.composable.SignupScreen

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
            route = Screen.RecipeDetailsScreen.route + "recipeId={recipeId}",
            arguments = listOf(
                navArgument(
                    name = "recipeId"
                ) {
                    type = NavType.StringType
                }
            )
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

        composable(
            route = Screen.LoginScreen.route + "lastDestination={lastDestination}",
            arguments = listOf(
                navArgument(
                    name = "lastDestination"
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            LoginScreen(navController = navController)
        }

        composable(
            route = Screen.SignupScreen.route + "lastDestination={lastDestination}",
            arguments = listOf(
                navArgument(
                    name = "lastDestination"
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            SignupScreen(navController = navController)
        }
    }
}