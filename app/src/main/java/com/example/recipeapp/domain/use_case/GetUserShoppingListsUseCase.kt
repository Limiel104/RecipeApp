package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetUserShoppingListsUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(userId: String, getRecipesFromRemote: Boolean): Flow<Resource<List<ShoppingList>>> {
        return shoppingListRepository.getUserShoppingLists(userId, getRecipesFromRemote)
    }
}