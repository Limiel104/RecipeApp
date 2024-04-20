package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.SearchQueryEntity
import com.example.recipeapp.domain.model.SearchQuery

fun SearchQueryEntity.toSearchQuery(): SearchQuery {
    return SearchQuery(
        searchQueryId = searchQueryId,
        query = query
    )
}

fun SearchQuery.toSearchQueryEntity(): SearchQueryEntity {
    return SearchQueryEntity(
        searchQueryId = 0,
        query = query
    )
}