package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class AddShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(shoppingListWithIngredients: ShoppingListWithIngredients): Flow<Resource<Boolean>> {
        return shoppingListRepository.addShoppingList(shoppingListWithIngredients)
    }
}