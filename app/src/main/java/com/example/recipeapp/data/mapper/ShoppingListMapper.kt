package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient
import com.example.recipeapp.data.remote.ShoppingListDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients

fun ShoppingListEntity.toShoppingList(): ShoppingList {
    return ShoppingList(
        shoppingListId = shoppingListId,
        name = name,
        createdBy = createdBy
    )
}

fun ShoppingListDto.toShoppingListEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        shoppingListId = shoppingListId,
        name = name,
        createdBy = createdBy
    )
}

fun ShoppingListDto.getShoppingListIngredientsList(): List<ShoppingListIngredientEntity> {
    val shoppingListIngredientsEntityList = mutableListOf<ShoppingListIngredientEntity>()

    for(ingredient in ingredientMap) {
        val newEntity = ShoppingListIngredientEntity(
            shoppingListIngredientId = 0,
            ingredientId = ingredient.key,
            shoppingListId = shoppingListId,
            quantity = ingredient.value
        )
        shoppingListIngredientsEntityList.add(newEntity)
    }

    return shoppingListIngredientsEntityList
}

fun ShoppingListWithIngredient.toShoppingListWithIngredients(ingredientList: List<Ingredient>): ShoppingListWithIngredients {
    val quantityList = shoppingListIngredients.map { it.quantity }
    val ingredients: Map<Ingredient,String> = ingredientList.zip(quantityList).toMap()

    return ShoppingListWithIngredients(
        shoppingListId = shoppingList.shoppingListId,
        name = shoppingList.name,
        createdBy = shoppingList.createdBy,
        ingredients = ingredients
    )
}