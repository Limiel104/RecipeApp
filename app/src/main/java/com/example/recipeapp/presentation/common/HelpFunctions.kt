package com.example.recipeapp.presentation.common

import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity

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
                category = "category"
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