package com.example.recipeapp.presentation.shopping_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.use_case.AddShoppingListUseCase
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
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val addShoppingListUseCase: AddShoppingListUseCase
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

            is ShoppingListEvent.OnIngredientClicked -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    clickedIngredientId = event.ingredientId,
                    isQuantityBottomSheetOpened = true
                )
            }

            is ShoppingListEvent.SelectedWholeQuantity -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    selectedWholeQuantity = event.whole
                )
            }

            is ShoppingListEvent.SelectedDecimalQuantity -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    selectedDecimalQuantity = event.decimal
                )
            }

            is ShoppingListEvent.SelectedTypeQuantity -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    selectedTypeQuantity = event.type
                )
            }

            is ShoppingListEvent.OnCheckBoxToggled -> {
                val tempMap = getTempMapBoolean(_shoppingListState.value.checkedIngredients)
                tempMap[event.ingredient] = !_shoppingListState.value.checkedIngredients[event.ingredient]!!

                _shoppingListState.value = shoppingListState.value.copy(
                    checkedIngredients = tempMap
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

            ShoppingListEvent.OnQuantityPickerDismissed -> {
                _shoppingListState.value = shoppingListState.value.copy(
                    selectedWholeQuantity = "",
                    selectedDecimalQuantity = "",
                    selectedTypeQuantity = "",
                    isQuantityBottomSheetOpened = false
                )
            }

            ShoppingListEvent.OnQuantityPickerSaved -> {
                val ingredient = _shoppingListState.value.shoppingListIngredients.keys.find { ingredient ->
                    ingredient.ingredientId == _shoppingListState.value.clickedIngredientId
                }

                val quantity = getIngredientQuantity(
                    _shoppingListState.value.selectedWholeQuantity,
                    _shoppingListState.value.selectedDecimalQuantity,
                    _shoppingListState.value.selectedTypeQuantity
                )

                val shoppingListIngredients = getTempMap(_shoppingListState.value.shoppingListIngredients)

                ingredient?.let {
                    if(shoppingListIngredients.keys.contains(ingredient))
                        shoppingListIngredients.replace(it, quantity)
                    else
                        shoppingListIngredients.put(it, quantity)
                }

                _shoppingListState.value = shoppingListState.value.copy(
                    shoppingListIngredients = shoppingListIngredients,
                    selectedWholeQuantity = "",
                    selectedDecimalQuantity = "",
                    selectedTypeQuantity = "",
                    isQuantityBottomSheetOpened = false
                )
            }

            ShoppingListEvent.OnAddShoppingList -> {
                addShoppingList()
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

    private fun getTempMapBoolean(checkedIngredients: Map<Ingredient, Boolean>): MutableMap<Ingredient, Boolean> {
        val tempMap = mutableMapOf<Ingredient, Boolean>()
        for(checkedIngredient in checkedIngredients) {
            tempMap[checkedIngredient.key] = checkedIngredient.value
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

    private fun getIngredientQuantity(whole: String, decimal: String, type: String): String {
        return if (whole != "" && decimal != "" && type != "") "$whole$decimal $type"
        else if(whole != "" && decimal != "") "$whole$decimal"
        else if(whole != "" && type != "") "$whole $type"
        else if(decimal != "" && type != "") "$decimal $type"
        else if(whole != "" ) whole
        else if(decimal != "") decimal
        else if(type != "") type
        else ""
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
                                allIngredients = response.data,
                                checkedIngredients = response.data.associateWith { false }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addShoppingList() {

        val shoppingListWithIngredients = ShoppingListWithIngredients(
            shoppingListId = "",
            name = "Some name",
            createdBy = getCurrentUserUseCase()!!.uid,
            ingredients = _shoppingListState.value.shoppingListIngredients
        )

        viewModelScope.launch {
            addShoppingListUseCase(shoppingListWithIngredients).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from addShoppingList: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get addShoppingList: ${response.isLoading}")
                        _shoppingListState.value = shoppingListState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString() + " addShoppingList")
                    }
                }
            }
        }
    }
}