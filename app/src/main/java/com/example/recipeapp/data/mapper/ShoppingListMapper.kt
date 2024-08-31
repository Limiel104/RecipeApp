package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient
import com.example.recipeapp.data.remote.ShoppingListDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients

fun ShoppingListEntity.toShoppingList(): ShoppingList {
    return ShoppingList(
        shoppingListId = shoppingListId,
        name = name,
        createdBy = createdBy,
        date = date
    )
}

fun ShoppingListDto.toShoppingListEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        shoppingListId = shoppingListId,
        name = name,
        createdBy = createdBy,
        date = date
    )
}

fun ShoppingListDto.getShoppingListIngredientsEntityList(): List<ShoppingListIngredientEntity> {
    val shoppingListIngredientsEntityList = mutableListOf<ShoppingListIngredientEntity>()

    for(ingredient in ingredientMap) {
        val newEntity = ShoppingListIngredientEntity(
            shoppingListIngredientId = 0,
            ingredientId = ingredient.key,
            shoppingListId = shoppingListId,
            quantity = ingredient.value,
            isChecked = checkedIngredientMap[ingredient.key]!!
        )
        shoppingListIngredientsEntityList.add(newEntity)
    }

    return shoppingListIngredientsEntityList
}

fun ShoppingListWithIngredient.toShoppingListWithIngredients(ingredientList: List<Ingredient>): ShoppingListWithIngredients {
    val quantityList = shoppingListIngredients.map { it.quantity }
    val ingredients: Map<Ingredient, Quantity> = ingredientList.zip(quantityList).toMap()
    val isCheckedList = shoppingListIngredients.map { it.isChecked }
    val checkedIngredients: Map<Ingredient, Boolean> = ingredientList.zip(isCheckedList).toMap()

    return ShoppingListWithIngredients(
        shoppingListId = shoppingList.shoppingListId,
        name = shoppingList.name,
        createdBy = shoppingList.createdBy,
        ingredients = ingredients,
        checkedIngredients = checkedIngredients,
        date = shoppingList.date
    )
}

fun ShoppingListWithIngredients.toShoppingListDto(documentId: String): ShoppingListDto {
    val ingredientIds = ingredients.keys.map { it.ingredientId }
    val quantities = ingredients.values
    val isCheckedList = checkedIngredients.values
    val ingredientMap: Map<String,String> = ingredientIds.zip(quantities).toMap()
    val checkedIngredientMap: Map<String, Boolean> = ingredientIds.zip(isCheckedList).toMap()

    return ShoppingListDto(
        shoppingListId = documentId,
        name = name,
        createdBy = createdBy,
        ingredientMap = ingredientMap,
        checkedIngredientMap = checkedIngredientMap,
        date = date
    )
}