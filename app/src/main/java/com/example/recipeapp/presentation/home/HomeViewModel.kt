package com.example.recipeapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository
): ViewModel() {

    init {
        getIngredients()
        getRecipes()
        getRecipeWithIngredients()
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
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG3",response.data.toString())
                    }
                }
            }
        }
    }
}