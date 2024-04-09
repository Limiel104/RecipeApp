package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredients
import com.example.recipeapp.domain.model.Recipe

fun RecipeWithIngredients.toRecipe(): Recipe {
    return Recipe(
        recipeId = recipe.recipeId,
        name = recipe.name,
        ingredientList = ingredients.map { it.toIngredient().name },
        prepTime = recipe.prepTime,
        servings = recipe.servings,
        description = recipe.description,
        isVegetarian = recipe.isVegetarian,
        isVegan = recipe.isVegan,
        imageUrl = recipe.imageUrl
    )
}

fun Recipe.toRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        recipeId = recipeId,
        name = name,
        prepTime = prepTime,
        servings = servings,
        description = description,
        isVegetarian = isVegetarian,
        isVegan = isVegan,
        imageUrl = imageUrl
    )
}

fun Recipe.getIngredientEntityList(): List<IngredientEntity> {
    val ingredientEntityList = mutableListOf<IngredientEntity>()

    for(ingredient in ingredientEntityList) {
        val newEntity = IngredientEntity(
            ingredientId = ingredient.ingredientId,
            name = ingredient.name,
            imageUrl = ingredient.imageUrl,
            category = ingredient.category
        )
        ingredientEntityList.add(newEntity)
    }

    return ingredientEntityList
}