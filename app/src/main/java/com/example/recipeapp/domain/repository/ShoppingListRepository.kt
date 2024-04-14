package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    suspend fun addShoppingList(shoppingListWithIngredients: ShoppingListWithIngredients): Flow<Resource<Boolean>>
    suspend fun getShoppingList(shoppingListId: String): Flow<Resource<ShoppingListWithIngredients>>
    suspend fun getUserShoppingLists(userId: String, getShoppingListFromRepository: Boolean): Flow<Resource<List<ShoppingList>>>
    suspend fun deleteShoppingList(shoppingListId: String): Flow<Resource<Boolean>>
}