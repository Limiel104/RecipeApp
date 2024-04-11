package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredientsQuantity
import com.example.recipeapp.data.remote.RecipeDto
import com.example.recipeapp.domain.model.Recipe

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

fun RecipeDto.toRecipeEntity(): RecipeEntity {
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

fun RecipeDto.getIngredientsQuantityList(): List<IngredientQuantityEntity> {
    val ingredientsQuantityEntityList = mutableListOf<IngredientQuantityEntity>()

    for(ingredient in ingredientsQuantityMap) {
        val newEntity = IngredientQuantityEntity(
            ingredientQuantityId = 0,
            ingredientId = ingredient.key,
            recipeId = recipeId,
            quantity = ingredient.value
        )
        ingredientsQuantityEntityList.add(newEntity)
    }

    return ingredientsQuantityEntityList
}

fun RecipeWithIngredientsQuantity.toRecipe(): Recipe {
    return Recipe(
        recipeId = recipe.recipeId,
        name = recipe.name,
        ingredientList = ingredientsQuantity.map { it.toIngredientQuantity(recipe.recipeId) },
        prepTime = recipe.prepTime,
        servings = recipe.servings,
        description = recipe.description,
        isVegetarian = recipe.isVegetarian,
        isVegan = recipe.isVegan,
        imageUrl = recipe.imageUrl
    )
}