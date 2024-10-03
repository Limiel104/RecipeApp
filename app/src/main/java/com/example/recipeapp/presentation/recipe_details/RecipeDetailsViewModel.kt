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
import com.example.recipeapp.domain.use_case.AddSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetRecipeUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val addSavedRecipeUseCase: AddSavedRecipeUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase,
    private val getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getSavedRecipeIdUseCase: GetSavedRecipeIdUseCase
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
        checkIfUserLoggedIn()
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

            RecipeDetailsEvent.OnSaveRecipe -> {
                if(_recipeDetailsState.value.isUserLoggedIn) {
                    if(_recipeDetailsState.value.isRecipeSaved)
                        getSavedRecipeId()
                    else
                        addSavedRecipe()
                }
            }

            RecipeDetailsEvent.OnGoBack -> {
                viewModelScope.launch {
                    _recipeDetailsUiEventChannel.send(RecipeDetailsUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _recipeDetailsState.value = recipeDetailsState.value.copy(
                isUserLoggedIn = currentUser != null
            )

            currentUser?.let {
                _recipeDetailsState.value = recipeDetailsState.value.copy(
                    userUID = currentUser.uid
                )
                getUserSavedRecipes()
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
            val newQuantityWithType = "%.2f $type".format(newQuantity).replace(',','.')

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

    private fun getUserSavedRecipes(
        userUID: String = _recipeDetailsState.value.userUID
    ) {
        viewModelScope.launch {
            getUserSavedRecipesUseCase(userUID, "", true).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from get user saved recipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get user saved recipes: ${response.isLoading}")
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            _recipeDetailsState.value = recipeDetailsState.value.copy(
                                isRecipeSaved = response.data.any { it.recipeId == _recipeDetailsState.value.recipe.recipeId }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getSavedRecipeId(
        userUID: String = _recipeDetailsState.value.userUID,
        recipeId: String = _recipeDetailsState.value.recipeId
    ) {
        viewModelScope.launch {
            getSavedRecipeIdUseCase(userUID, recipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from get user saved recipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get user saved recipes: ${response.isLoading}")
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            deleteSavedRecipe(response.data)
                        }
                    }
                }
            }
        }
    }

    private fun addSavedRecipe(
        userUID: String = _recipeDetailsState.value.userUID,
        recipeId: String = _recipeDetailsState.value.recipeId
    ) {
        viewModelScope.launch {
            addSavedRecipeUseCase(userUID, recipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from add saved recipe: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading add saved recipe: ${response.isLoading}")
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isRecipeSaved = true
                        )
                        getUserSavedRecipes()
                    }
                }
            }
        }
    }

    private fun deleteSavedRecipe(savedRecipeId: String) {
        viewModelScope.launch {
            deleteSavedRecipeUseCase(savedRecipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message delete saved recipe: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading delete saved recipe: ${response.isLoading}")
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        _recipeDetailsState.value = recipeDetailsState.value.copy(
                            isRecipeSaved = false
                        )
                        getUserSavedRecipes()
                    }
                }
            }
        }
    }
}