package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredient
import com.example.recipeapp.data.remote.RecipeDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.RecipeWithIngredients

fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        recipeId = recipeId,
        name = name,
        prepTime = prepTime,
        servings = servings,
        description = description,
        isVegetarian = isVegetarian,
        isVegan = isVegan,
        imageUrl = imageUrl,
        createdBy = createdBy
    )
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
        imageUrl = imageUrl,
        createdBy = createdBy
    )
}

fun RecipeDto.getRecipeIngredientsList(): List<RecipeIngredientEntity> {
    val recipeIngredientsEntityList = mutableListOf<RecipeIngredientEntity>()

    for(ingredient in ingredientMap) {
        val newEntity = RecipeIngredientEntity(
            recipeIngredientId = 0,
            ingredientId = ingredient.key,
            recipeId = recipeId,
            quantity = ingredient.value
        )
        recipeIngredientsEntityList.add(newEntity)
    }

    return recipeIngredientsEntityList
}

fun RecipeWithIngredient.toRecipeWithIngredients(ingredientList: List<Ingredient>): RecipeWithIngredients {
    val quantityList = recipeIngredients.map { it.quantity }
    val ingredients: Map<Ingredient,String> = ingredientList.zip(quantityList).toMap()

    return RecipeWithIngredients(
        recipeId = recipe.recipeId,
        name = recipe.name,
        ingredients = ingredients,
        prepTime = recipe.prepTime,
        servings = recipe.servings,
        description = recipe.description,
        isVegetarian = recipe.isVegetarian,
        isVegan = recipe.isVegan,
        imageUrl = recipe.imageUrl,
        createdBy = recipe.createdBy
    )
}
