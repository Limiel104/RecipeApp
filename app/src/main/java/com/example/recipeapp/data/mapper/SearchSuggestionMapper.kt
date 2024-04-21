package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.SearchSuggestionEntity
import com.example.recipeapp.domain.model.SearchSuggestion

fun SearchSuggestionEntity.toSearchSuggestion(): SearchSuggestion {
    return SearchSuggestion(
        searchSuggestionId = searchSuggestionId,
        text = text
    )
}

fun SearchSuggestion.toSearchSuggestionEntity(): SearchSuggestionEntity {
    return SearchSuggestionEntity(
        searchSuggestionId = 0,
        text = text
    )
}