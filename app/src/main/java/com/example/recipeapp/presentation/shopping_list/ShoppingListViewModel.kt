package com.example.recipeapp.presentation.shopping_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getIngredientsUseCase: GetIngredientsUseCase
): ViewModel() {
    
    private val _shoppingListState = mutableStateOf(ShoppingListState())
    val shoppingListState: State<ShoppingListState> = _shoppingListState

    private val _shoppingListUiEventChannel = Channel<ShoppingListUiEvent>()
    val shoppingListUiEventChannelFlow = _shoppingListUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "ShoppingList ViewModel")
        checkIfUserLoggedIn()
    }

    fun onEvent(event: ShoppingListEvent) {
        when(event) {
            is ShoppingListEvent.EnteredIngredient -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    ingredient = event.ingredient
                )
            }

            is ShoppingListEvent.SelectedIngredient -> {
                val tempList = getTempList(_shoppingListState.value.selectedIngredients)
                tempList.add(event.selectedIngredient)

                _shoppingListState.value = shoppingListState.value.copy(
                    isDropDownMenuExpanded = false,
                    selectedIngredients = tempList
                )

                val tempList2 = getTempList(shoppingListState.value.shoppingListIngredients.keys.toList())
                tempList2.addAll(_shoppingListState.value.selectedIngredients)

                _shoppingListState.value = shoppingListState.value.copy(
                    ingredientsToSelect = getCurrentIngredients(tempList2.associateWith { "" })
                )
            }

            ShoppingListEvent.OnAddButtonClicked -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    isAddIngredientsDialogOpened = true
                )
            }

            ShoppingListEvent.OnDropDownMenuExpandChange -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    isDropDownMenuExpanded = !shoppingListState.value.isDropDownMenuExpanded
                )
            }

            ShoppingListEvent.OnAddIngredientsDialogDismiss -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    isAddIngredientsDialogOpened = false,
                    selectedIngredients = emptyList(),
                    ingredientsToSelect = getCurrentIngredients(getTempMap(_shoppingListState.value.shoppingListIngredients))
                )
            }

            ShoppingListEvent.OnAddIngredientsDialogSave -> {
                addIngredientsToShoppingList(_shoppingListState.value.selectedIngredients)
            }

            ShoppingListEvent.OnLogin -> {
                viewModelScope.launch {
                    _shoppingListUiEventChannel.send(ShoppingListUiEvent.NavigateToLogin)
                }
            }

            ShoppingListEvent.OnSignup -> {
                viewModelScope.launch {
                    _shoppingListUiEventChannel.send(ShoppingListUiEvent.NavigateToSignup)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _shoppingListState.value = shoppingListState.value.copy(
                isUserLoggedIn = currentUser != null
            )

            currentUser?.let {
                getIngredients()
            }
        }
    }

    private fun getTempList(selectedIngredients: List<Ingredient>): MutableList<Ingredient> {
        val tempList = mutableListOf<Ingredient>()
        for(ingredient in selectedIngredients) {
            tempList.add(ingredient)
        }
        return  tempList
    }

    private fun getTempMap(shoppingListIngredients: Map<Ingredient, Quantity>): MutableMap<Ingredient, Quantity> {
        val tempMap = mutableMapOf<Ingredient, Quantity>()
        for(shoppingListIngredient in shoppingListIngredients) {
            tempMap[shoppingListIngredient.key] = shoppingListIngredient.value
        }
        return tempMap
    }

    private fun getCurrentIngredients(shoppingListIngredients: Map<Ingredient, Quantity>): List<Ingredient> {
        val allIngredients = _shoppingListState.value.allIngredients

        return allIngredients.filter { ingredient -> shoppingListIngredients.all { shoppingListIngredient ->  shoppingListIngredient.key.ingredientId != ingredient.ingredientId}  }
    }

    private fun addIngredientsToShoppingList(newIngredients: List<Ingredient>) {
        val shoppingListIngredients = getTempMap(_shoppingListState.value.shoppingListIngredients)

        for(newIngredient in newIngredients)
            shoppingListIngredients[newIngredient] = ""

        _shoppingListState.value = shoppingListState.value.copy(
            ingredientsToSelect = getCurrentIngredients(shoppingListIngredients),
            shoppingListIngredients = shoppingListIngredients,
            selectedIngredients = emptyList(),
            isAddIngredientsDialogOpened = false
        )
    }

    private fun getIngredients() {
        viewModelScope.launch {
            getIngredientsUseCase().collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getIngredients: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get ingredients: ${response.isLoading}")
                        _shoppingListState.value = shoppingListState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            _shoppingListState.value = shoppingListState.value.copy(
                                ingredientsToSelect = response.data,
                                allIngredients = response.data
                            )
                        }
                    }
                }
            }
        }
    }
}