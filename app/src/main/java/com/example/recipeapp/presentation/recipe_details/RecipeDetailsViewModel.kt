package com.example.recipeapp.presentation.recipe_details

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase
): ViewModel() {

    private val _recipeDetailsState = mutableStateOf(RecipeDetailsState())
    val recipeDetailsState: State<RecipeDetailsState> = _recipeDetailsState

    init {
        Log.i("TAG", "RecipeDetails ViewModel")
        viewModelScope.launch { getRecipe("") }
    }

    fun onEvent(event: RecipeDetailsEvent) {
        when(event) {
            is RecipeDetailsEvent.OnTabChanged -> {
                _recipeDetailsState.value = recipeDetailsState.value.copy(
                    secondaryTabState = event.tabId
                )
            }
        }
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
                                recipe = response.data
                            )
                        }
                    }
                }
            }
        }
    }
}