package com.example.recipeapp.presentation.home

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchQuery

data class HomeState(
    val recipes: List<Recipe> = emptyList(),
    val query: String = "",
    val recentSearchQueries: List<SearchQuery> = emptyList(),
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = false
)