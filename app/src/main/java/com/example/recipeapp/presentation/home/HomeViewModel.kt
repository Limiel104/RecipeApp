package com.example.recipeapp.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getUserShoppingListsUseCase: GetUserShoppingListsUseCase,
    private val addSearchSuggestionUseCase: AddSearchSuggestionUseCase,
    private val getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val sortRecipesUseCase: SortRecipesUseCase
): ViewModel() {

    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState

    private val _homeUiEventChannel = Channel<HomeUiEvent>()
    val homeUiEventChannelFlow = _homeUiEventChannel.receiveAsFlow()

    init {
        getCategories()
        getRecipes(true)
        getIngredients()
        getShoppingLists(true)
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OnRecipeSelected -> {
                viewModelScope.launch {
                    Log.i("TAG","Clicked recipeId: ${event.recipeId}")
                    _homeUiEventChannel.send(HomeUiEvent.NavigateToRecipeDetails(event.recipeId))
                }
            }

            is HomeEvent.OnQueryChange -> {
                Log.i("TAG","New query: ${event.query}")
                _homeState.value = homeState.value.copy(
                    query = event.query
                )
            }

            is HomeEvent.OnSearchSuggestionClicked -> {
                _homeState.value = homeState.value.copy(
                    query = event.suggestionText
                )
            }

            is HomeEvent.OnCategoryClicked -> {
                if(_homeState.value.selectedCategory == event.categoryId) {
                    _homeState.value = homeState.value.copy(
                        selectedCategory = ""
                    )
                    getRecipes(false)
                }
                else {
                    _homeState.value = homeState.value.copy(
                        selectedCategory = event.categoryId
                    )
                    getRecipes(false)
                }
                Log.i("TAG",_homeState.value.selectedCategory)
            }

            is HomeEvent.OnSortRecipes -> {
                _homeState.value = homeState.value.copy(
                    recipesOrder = event.recipeOrder
                )

                _homeState.value = homeState.value.copy(
                    recipes = sortRecipesUseCase(event.recipeOrder, _homeState.value.recipes)
                )
            }

            HomeEvent.OnActiveChange -> {
                val isSearchActive = !_homeState.value.isSearchActive

                _homeState.value = homeState.value.copy(
                    isSearchActive = isSearchActive
                )

                if(isSearchActive) {
                    getSearchSuggestions()
                }
            }

            HomeEvent.OnSearchClicked -> {
                _homeState.value = homeState.value.copy(
                    isSearchActive = false
                )

                addSearchSuggestion(_homeState.value.query)
                getRecipes(false)
            }

            HomeEvent.OnClearClicked -> {
                val query = _homeState.value.query

                if(query.isNotEmpty()) {
                    _homeState.value = homeState.value.copy(
                        query = ""
                    )
                }
                else {
                    _homeState.value = homeState.value.copy(
                        isSearchActive = false
                    )
                    getRecipes(false)
                }
            }
        }
    }

    private fun getRecipes(
        getRecipesFromRemote: Boolean,
        query: String = _homeState.value.query,
        category: String = _homeState.value.selectedCategory
    ) {
        viewModelScope.launch {
            getRecipesUseCase(getRecipesFromRemote, query, category).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getRecipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading recipes: ${response.isLoading}")
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG2",response.data.toString())
                        response.data?.let {
                            _homeState.value = homeState.value.copy(
                                recipes = sortRecipesUseCase(_homeState.value.recipesOrder, response.data)
                            )
                        }
                    }
                }
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
                        Log.i("TAG","Loading ingredients: ${response.isLoading}")
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG1",response.data.toString())
                    }
                }
            }
        }
    }

    private fun getShoppingLists(getShoppingListsFromRemote: Boolean) {
        viewModelScope.launch {
            getUserShoppingListsUseCase("userId", getShoppingListsFromRemote).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getShoppingLists: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading shopping lists: ${response.isLoading}")
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG4",response.data.toString())
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
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG4","Search suggestion added")
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
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _homeState.value = homeState.value.copy(
                                searchSuggestions = response.data
                            )
                        }
                        Log.i("TAG4",_homeState.value.searchSuggestions.toString())
                    }
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getCategories: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading categories: ${response.isLoading}")
                        _homeState.value = homeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _homeState.value = homeState.value.copy(
                                categories = response.data
                            )
                        }
                    }
                }
            }
        }
    }
}