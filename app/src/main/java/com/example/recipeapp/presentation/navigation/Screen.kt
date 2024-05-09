package com.example.recipeapp.presentation.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object ShoppingListScreen: Screen("shopping_list_screen")
    object AccountScreen: Screen("account_screen")
    object RecipeDetailsScreen: Screen("recipe_details_screen")
    object AddRecipeScreen: Screen("add_recipe_screen")
    object SavedRecipesScreen: Screen("saved_recipes_screen")
    object LoginScreen: Screen("login_screen")
    object SignupScreen: Screen("signup_screen")
}