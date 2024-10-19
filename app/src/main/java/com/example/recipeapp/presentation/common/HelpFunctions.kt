package com.example.recipeapp.presentation.common

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.model.ShoppingList

const val ingredient = "Ingredient Name"
const val ingredient2 = "Ingredient2 Name"
const val ingredient3 = "Ingredient3 Name"

fun getIngredients(): List<Ingredient> {
    return listOf(
        Ingredient(
            ingredientId = "ingredientId",
            name = ingredient,
            imageUrl = "imageUrl",
            category = "category"
        ),
        Ingredient(
            ingredientId = "ingredient2Id",
            name = ingredient2,
            imageUrl = "imageUrl",
            category = "category2"
        ),
        Ingredient(
            ingredientId = "ingredient3Id",
            name = ingredient3,
            imageUrl = "imageUrl",
            category = "category"
        )
    )
}

fun getIngredientsWithQuantity(): Map<Ingredient, Quantity> {
    return mapOf(
        Pair(
            Ingredient(
                ingredientId = "ingredientId",
                name = ingredient,
                imageUrl = "imageUrl",
                category = "category"
            ),
            "200.0 g"
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient2Id",
                name = ingredient2,
                imageUrl = "imageUrl",
                category = "category2"
            ),
            "5.0 kg"
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient3Id",
                name = ingredient3,
                imageUrl = "imageUrl",
                category = "category"
            ),
            "1 cup"
        )
    )
}

fun getIngredientsWithBoolean(): Map<Ingredient, Boolean> {
    return mapOf(
        Pair(
            Ingredient(
                ingredientId = "ingredientId",
                name = ingredient,
                imageUrl = "imageUrl",
                category = "category"
            ),
            false
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient2Id",
                name = ingredient2,
                imageUrl = "imageUrl",
                category = "category2"
            ),
            true
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient3Id",
                name = ingredient3,
                imageUrl = "imageUrl",
                category = "category"
            ),
            false
        )
    )
}

fun getShoppingLists(): List<ShoppingList> {
    return listOf(
        ShoppingList(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userUID",
            date = 1234564398
        ),
        ShoppingList(
            shoppingListId = "shoppingList2Id",
            name = "Shopping List 2 Name",
            createdBy = "userUID",
            date = 1234345349
        ),
        ShoppingList(
            shoppingListId = "shoppingList3Id",
            name = "Shopping List 3 Name",
            createdBy = "userUID",
            date = 1234324345
        ),
        ShoppingList(
            shoppingListId = "shoppingList4Id",
            name = "Shopping List 4 Name",
            createdBy = "userUID",
            date = 1234324357
        ),
        ShoppingList(
            shoppingListId = "shoppingList5Id",
            name = "Shopping List 5 Name",
            createdBy = "userUID",
            date = 1234824354
        )
    )
}

fun getRecipes(): List<Recipe> {
    return listOf(
        Recipe(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Chicken"),
            date = 1231236
        ),
        Recipe(
            recipeId = "recipe2Id",
            name = "Recipe2 Name",
            prepTime = "10 min",
            servings = 3,
            description = "Recipe2 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Soup","Chicken"),
            date = 1231234
        ),
        Recipe(
            recipeId = "recipe3Id",
            name = "Recipe3 Name",
            prepTime = "25 min",
            servings = 4,
            description = "Recipe3 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Soup"),
            date = 1231235
        ),
        Recipe(
            recipeId = "recipe4Id",
            name = "Recipe4 Name",
            prepTime = "2 h 45 min",
            servings = 2,
            description = "Recipe4 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Appetizer"),
            date = 1231237
        ),
        Recipe(
            recipeId = "recipe5Id",
            name = "Recipe5 Name",
            prepTime = "1 h",
            servings = 1,
            description = "Recipe5 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Stew","Dinner"),
            date = 1231238
        ),
        Recipe(
            recipeId = "recipe6Id",
            name = "Recipe6 Name",
            prepTime = "30 min",
            servings = 1,
            description = "Recipe6 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Stew"),
            date = 1231239
        )
    )
}

fun getCategories(): List<Category> {
    return listOf(
        Category("Appetizer",""),
        Category("Chicken",""),
        Category("Dinner",""),
        Category("Soup",""),
        Category("Stew","")
    )
}

fun getSearchSuggestions(): List<SearchSuggestion> {
    return listOf(
        SearchSuggestion(
            searchSuggestionId = 1,
            text = "Search Suggestion Text"
        ),
        SearchSuggestion(
            searchSuggestionId = 2,
            text = "Search Suggestion 2 Text"
        ),
        SearchSuggestion(
            searchSuggestionId = 3,
            text = "Search Suggestion 3 Text"
        )
    )
}