package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.RecipeCategoryEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithCategory
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
        createdBy = createdBy,
        categories = emptyList()
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

fun RecipeWithIngredient.toRecipeWithIngredients(ingredientList: List<Ingredient>, categoryList: List<String>): RecipeWithIngredients {
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
        createdBy = recipe.createdBy,
        categories = categoryList
    )
}

fun RecipeWithIngredients.toRecipeDto(documentId: String): RecipeDto {
    val keys = ingredients.keys.map { it.ingredientId }
    val values = ingredients.values
    val ingredientMap: Map<String,String> = keys.zip(values).toMap()

    return RecipeDto(
        recipeId = documentId,
        name = name,
        ingredientMap = ingredientMap,
        prepTime = prepTime,
        servings = servings,
        description = description,
        isVegetarian = isVegetarian,
        isVegan = isVegan,
        imageUrl = imageUrl,
        createdBy = createdBy,
        categoryList = categories
    )
}

fun RecipeDto.getRecipeCategoryList(): List<RecipeCategoryEntity> {
    val categories = mutableListOf<RecipeCategoryEntity>()

    for(category in categoryList) {
        val newEntity = RecipeCategoryEntity(
            categoryId = 0,
            categoryName = category,
            recipeId = recipeId
        )
        categories.add(newEntity)
    }

    return categories
}

fun RecipeWithCategory.toRecipe(): Recipe {
    val categoryList = mutableListOf<String>()

    for(category in categories) {
        categoryList.add(category.categoryName)
    }

    return Recipe(
        recipeId = recipe.recipeId,
        name = recipe.name,
        prepTime = recipe.prepTime,
        servings = recipe.servings,
        description = recipe.description,
        isVegetarian = recipe.isVegetarian,
        isVegan = recipe.isVegan,
        imageUrl = recipe.imageUrl,
        createdBy = recipe.createdBy,
        categories = categoryList
    )
}