package com.example.recipeapp.presentation.common

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.ShoppingList

fun getIngredientsWithQuantity(): Map<Ingredient, Quantity> {
    return mapOf(
        Pair(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            "200.0 g"
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient2 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            "5.0 kg"
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient3 Name",
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
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            false
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient2 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            true
        ),
        Pair(
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient3 Name",
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