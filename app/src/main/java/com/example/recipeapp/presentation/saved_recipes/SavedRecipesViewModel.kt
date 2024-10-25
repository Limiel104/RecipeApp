package com.example.recipeapp.presentation.saved_recipes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase,
    private val getSavedRecipeIdUseCase: GetSavedRecipeIdUseCase,
    private val addSearchSuggestionUseCase: AddSearchSuggestionUseCase,
    private val getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase,
    private val sortRecipesUseCase: SortRecipesUseCase,
    ): ViewModel() {

    private val _savedRecipesState = mutableStateOf(SavedRecipesState())
    val savedRecipesState: State<SavedRecipesState> = _savedRecipesState

    private val _savedRecipesUiEventChannel = Channel<SavedRecipesUiEvent>()
    val savedRecipesUiEventChannelFlow = _savedRecipesUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "Saved Recipes ViewModel")
        checkIfUserLoggedIn()
    }

    fun onEvent(event: SavedRecipesEvent) {
        when(event) {
            is SavedRecipesEvent.OnRemove -> {
                getSavedRecipeId(recipeId = event.recipeId)
            }

            is SavedRecipesEvent.OnQueryChange -> {
                _savedRecipesState.value = savedRecipesState.value.copy(
                    query = event.query
                )
            }

            is SavedRecipesEvent.OnSortRecipes -> {
                _savedRecipesState.value = savedRecipesState.value.copy(
                    recipesOrder = event.recipeOrder
                )

                _savedRecipesState.value = savedRecipesState.value.copy(
                    savedRecipes = sortRecipesUseCase(event.recipeOrder, _savedRecipesState.value.savedRecipes)
                )
            }

            SavedRecipesEvent.OnActiveChange -> {
                val isSearchActive = !_savedRecipesState.value.isSearchActive
                _savedRecipesState.value = savedRecipesState.value.copy(
                    isSearchActive = isSearchActive
                )

                if(isSearchActive) { getSearchSuggestions() }
            }

            SavedRecipesEvent.OnSearchClicked -> {
                _savedRecipesState.value = savedRecipesState.value.copy(
                    isSearchActive = false
                )

                addSearchSuggestion(_savedRecipesState.value.query)
                getUserSavedRecipes()
            }

            SavedRecipesEvent.OnClearClicked -> {
                val query = _savedRecipesState.value.query

                if(query.isNotEmpty()) {
                    _savedRecipesState.value = savedRecipesState.value.copy(
                        query = ""
                    )
                }
                else {
                    _savedRecipesState.value = savedRecipesState.value.copy(
                        isSearchActive = false
                    )
                    getUserSavedRecipes()
                }
            }

            is SavedRecipesEvent.OnSearchSuggestionClicked -> {
                _savedRecipesState.value = savedRecipesState.value.copy(
                    query = event.suggestionText
                )
            }

            is SavedRecipesEvent.OnRecipeSelected -> {
                viewModelScope.launch { _savedRecipesUiEventChannel.send(SavedRecipesUiEvent.NavigateToRecipeDetails(event.recipeId)) }
            }

            SavedRecipesEvent.OnLogin -> {
                viewModelScope.launch {
                    _savedRecipesUiEventChannel.send(SavedRecipesUiEvent.NavigateToLogin)
                }
            }

            SavedRecipesEvent.OnSignup -> {
                viewModelScope.launch {
                    _savedRecipesUiEventChannel.send(SavedRecipesUiEvent.NavigateToSignup)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _savedRecipesState.value = savedRecipesState.value.copy(
                isUserLoggedIn = currentUser != null
            )

            Log.i("TAG456","ELEOELEOEKENEN")

            currentUser?.let {
                _savedRecipesState.value = savedRecipesState.value.copy(
                    userUID = currentUser.uid
                )
                getUserSavedRecipes(currentUser.uid)
            }
        }
    }

    private fun getUserSavedRecipes(
        userUID: String = _savedRecipesState.value.userUID,
        query: String = _savedRecipesState.value.query
    ) {
        viewModelScope.launch {
            getUserSavedRecipesUseCase(userUID, query,true).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from get user saved recipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get user saved recipes: ${response.isLoading}")
                        _savedRecipesState.value = savedRecipesState.value.copy(
                            isLoading = response.isLoading,
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            _savedRecipesState.value = savedRecipesState.value.copy(
                                savedRecipes = sortRecipesUseCase(_savedRecipesState.value.recipesOrder,response.data)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getSavedRecipeId(
        userUID: String = _savedRecipesState.value.userUID,
        recipeId: String
    ) {
        viewModelScope.launch {
            getSavedRecipeIdUseCase(userUID, recipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from get user saved recipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get user saved recipes: ${response.isLoading}")
                        _savedRecipesState.value = savedRecipesState.value.copy(
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

    private fun deleteSavedRecipe(savedRecipeId: String) {
        viewModelScope.launch {
            deleteSavedRecipeUseCase(savedRecipeId).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message delete saved recipe: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading delete saved recipe: ${response.isLoading}")
                        _savedRecipesState.value = savedRecipesState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        getUserSavedRecipes()
                    }
                }
            }
        }
    }

    private fun addSearchSuggestion(query: String) {
        val searchSuggestion = SearchSuggestion(
            searchSuggestionId = 0,
            text = query
        )

        viewModelScope.launch {
            addSearchSuggestionUseCase(searchSuggestion).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from addSearchSuggestion: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading add search suggestion: ${response.isLoading}")
                        _savedRecipesState.value = savedRecipesState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG","Search suggestion added")
                    }
                }
            }
        }
    }

    private fun getSearchSuggestions() {
        viewModelScope.launch {
            getSearchSuggestionsUseCase().collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getSearchSuggestions: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading search suggestions: ${response.isLoading}")
                        _savedRecipesState.value = savedRecipesState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _savedRecipesState.value = savedRecipesState.value.copy(
                                searchSuggestions = response.data
                            )
                        }
                        Log.i("TAG",_savedRecipesState.value.searchSuggestions.toString())
                    }
                }
            }
        }
    }
}