package com.example.recipeapp.presentation.recipe_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase
): ViewModel() {

    private val _recipeDetailsState = mutableStateOf(RecipeDetailsState())
    val recipeDetailsState: State<RecipeDetailsState> = _recipeDetailsState

    private val _recipeDetailsUiEventChannel = Channel<RecipeDetailsUiEvent>()
    val recipeDetailsUiEventChannelFlow = _recipeDetailsUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "RecipeDetails ViewModel")

        savedStateHandle.get<String>("recipeId")?.let { recipeId ->
            _recipeDetailsState.value = recipeDetailsState.value.copy(
                recipeId = recipeId
            )
        }
        viewModelScope.launch { getRecipe(_recipeDetailsState.value.recipeId) }
    }

    fun onEvent(event: RecipeDetailsEvent) {
        when(event) {
            is RecipeDetailsEvent.OnTabChanged -> {
                _recipeDetailsState.value = recipeDetailsState.value.copy(
                    secondaryTabState = event.tabId
                )
            }

            RecipeDetailsEvent.OnLessServings -> {
                _recipeDetailsState.value = recipeDetailsState.value.copy(
                    displayedServings = _recipeDetailsState.value.displayedServings - 1
                )

                recalculateIngredientsQuantity(
                    newServings = _recipeDetailsState.value.displayedServings
                )
            }

            RecipeDetailsEvent.OnMoreServings -> {
                _recipeDetailsState.value = recipeDetailsState.value.copy(
                    displayedServings = _recipeDetailsState.value.displayedServings + 1
                )

                recalculateIngredientsQuantity(
                    newServings = _recipeDetailsState.value.displayedServings
                )
            }

            RecipeDetailsEvent.OnGoBack -> {
                viewModelScope.launch {
                    _recipeDetailsUiEventChannel.send(RecipeDetailsUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun recalculateIngredientsQuantity(
        newServings: Int,
        recipeServings: Int = _recipeDetailsState.value.recipe.servings,
        ingredients: Map<Ingredient, Quantity> = _recipeDetailsState.value.recipe.ingredients
    ) {
        val recalculatedIngredients = mutableMapOf<Ingredient, Quantity>()

        for(ingredient in ingredients) {
            val quantity = ingredient.value.substringBefore(" ").toDouble()
            val type = ingredient.value.substringAfter(" ")

            val quantityForOneServing = quantity / recipeServings
            val newQuantity = quantityForOneServing * newServings
            val newQuantityWithType = "%.2f $type".format(newQuantity)

            recalculatedIngredients[ingredient.key] = newQuantityWithType
        }

        _recipeDetailsState.value = recipeDetailsState.value.copy(
            displayedIngredients = recalculatedIngredients
        )
    }

    private fun getRecipe(recipeId: String) {
        viewModelScope.launch {
            getRecipeUseCase(recipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getRecipe: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading recipe: ${response.isLoading}")
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            _recipeDetailsState.value = recipeDetailsState.value.copy(
                                recipe = response.data,
                                displayedServings = response.data.servings,
                                displayedIngredients = response.data.ingredients
                            )
                        }
                    }
                }
            }
        }
    }
}