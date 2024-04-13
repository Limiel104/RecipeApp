package com.example.recipeapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val shoppingListRepository: ShoppingListRepository
): ViewModel() {

    init {
        getIngredients()
        getRecipes()
        getRecipeWithIngredients()
        getShoppingLists()
        getShoppingListWithIngredients()
    }

    private fun getIngredients() {
        viewModelScope.launch {
            ingredientRepository.getIngredients().collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG1",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            recipeRepository.getRecipes().collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG2",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getRecipeWithIngredients() {
        viewModelScope.launch {
            recipeRepository.getRecipe("1").collect { response ->
                when(response) {
                    is Resource.Error -> {
                        response.message?.let { Log.i("TAG", it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG3",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getShoppingLists() {
        viewModelScope.launch {
            shoppingListRepository.getUserShoppingLists("user1").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG4",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getShoppingListWithIngredients() {
        viewModelScope.launch {
            shoppingListRepository.getShoppingList("slid1").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG5",response.data.toString())
                    }
                }
            }
        }
    }
}