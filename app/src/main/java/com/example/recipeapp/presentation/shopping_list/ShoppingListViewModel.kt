package com.example.recipeapp.presentation.shopping_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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