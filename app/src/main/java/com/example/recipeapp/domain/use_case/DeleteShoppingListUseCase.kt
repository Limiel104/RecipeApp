package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class DeleteShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(shoppingListId: String): Flow<Resource<Boolean>> {
        return shoppingListRepository.deleteShoppingList(shoppingListId)
    }
}