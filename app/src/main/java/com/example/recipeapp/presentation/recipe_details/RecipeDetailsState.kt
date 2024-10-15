package com.example.recipeapp.presentation.recipe_details

import com.example.recipeapp.data.remote.Quantity
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.RecipeWithIngredients

data class RecipeDetailsState(
    val recipeId: String = "",
    val recipe: RecipeWithIngredients = RecipeWithIngredients(
        recipeId = "",
        name = "",
        ingredients = emptyMap<Ingredient, Quantity>(),
        prepTime = "",
        servings = 0,
        description = "",
        isVegetarian = false,
        isVegan = false,
        imageUrl = "",
        createdBy = "",
        categories = emptyList<String>(),
        date = 0
    ),
    val secondaryTabState: Int = 0,
    val tabTitleList: List<String> = listOf("Ingredients", "Description"),
    val isLoading: Boolean = false,
    val displayedServings: Int = 0,
    val displayedIngredients: Map<Ingredient, Quantity> = emptyMap(),
    val isUserLoggedIn: Boolean = false,
    val userUID: String = "",
    val isRecipeSaved: Boolean = false
)