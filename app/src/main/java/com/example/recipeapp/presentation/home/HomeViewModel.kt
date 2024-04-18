package com.example.recipeapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val recipeRepository: RecipeRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val savedRecipeRepository: SavedRecipeRepository
): ViewModel() {

    val ingredient = Ingredient(
        ingredientId = "ingr2",
        name = "nowy",
        imageUrl = "url",
        category = "catcat"
    )

    val ingredient2 = Ingredient(
        ingredientId = "ingr3",
        name = "nowy2",
        imageUrl = "url",
        category = "catcat"
    )

    val ingredient3 = Ingredient(
        ingredientId = "ingr4",
        name = "nowy3",
        imageUrl = "url",
        category = "catcat"
    )

    init {
//        getIngredients()
//        getRecipeWithIngredients()

//        getShoppingListWithIngredients()
//        addRecipe()
        getRecipes(false)
//        getShoppingLists(true)
//        deleteRecipe()
//        addShoppingList()
//        deleteShoppingList()
//        addSavedRecipe()
//        deleteSavedRecipe()
        getSavedRecipes(true)
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

    private fun getRecipes(getRecipesFromRemote: Boolean) {
        viewModelScope.launch {
            recipeRepository.getRecipes(getRecipesFromRemote).collect { response ->
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

    private fun getShoppingLists(getShoppingListsFromRemote: Boolean) {
        viewModelScope.launch {
            shoppingListRepository.getUserShoppingLists("user1", getShoppingListsFromRemote).collect { response ->
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

    private fun addRecipe() {
        val recipe = RecipeWithIngredients(
            recipeId = "0",
            name = "nowy przepis lalaldbd",
            ingredients = mapOf(
                ingredient to "4 g",
                ingredient2 to "32 g",
                ingredient3 to "7 kg"
            ),
            prepTime = "40 min",
            servings = 4,
            description = "ala ma kota, kot ma aleegretg ye5ryer",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "url",
            createdBy = "user1"
        )

        viewModelScope.launch {
            recipeRepository.addRecipe(recipe).collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG6",response.data.toString())
                    }
                }
            }
        }
    }

    private fun deleteRecipe() {
        viewModelScope.launch {
            recipeRepository.deleteRecipe("fJX844TXZzB07CXdwAk1").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG7",response.data.toString())
                    }
                }
            }
        }
    }

    private fun addShoppingList() {
        val shoppingList = ShoppingListWithIngredients(
            shoppingListId = "0",
            name = "shopping list name",
            createdBy = "user1",
            ingredients = mapOf(
                ingredient to "4 g",
                ingredient3 to "7 kg"
            )
        )

        viewModelScope.launch {
            shoppingListRepository.addShoppingList(shoppingList).collect {
                    response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG8",response.data.toString())
                    }
                }
            }
        }
    }

    private fun deleteShoppingList() {
        viewModelScope.launch {
            shoppingListRepository.deleteShoppingList("OxJu1QACWN2cG7QHttSE").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG9",response.data.toString())
                    }
                }
            }
        }
    }

    private fun addSavedRecipe() {
        viewModelScope.launch {
            savedRecipeRepository.addSavedRecipe("user1","dsasd").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG10",response.data.toString())
                    }
                }
            }
        }
    }

    private fun deleteSavedRecipe() {
        viewModelScope.launch {
            savedRecipeRepository.deleteSavedRecipe("5BtTUrCcRlPxCR1INE4z").collect { response ->
                when(response) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        Log.i("TAG11",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getSavedRecipes(getSavedRecipesFromRemote: Boolean) {
        viewModelScope.launch {
            savedRecipeRepository.getUserSavedRecipes("user1", getSavedRecipesFromRemote).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG EE 12",response.message.toString())

                    }
                    is Resource.Loading -> {
                        Log.i("TAG FF 12",response.isLoading.toString())

                    }
                    is Resource.Success -> {
                        Log.i("TAG12",response.data.toString())
                    }
                }
            }
        }
    }
}