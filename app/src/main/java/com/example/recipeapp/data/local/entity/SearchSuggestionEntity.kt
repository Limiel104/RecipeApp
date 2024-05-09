package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchSuggestionEntity(
    @PrimaryKey(autoGenerate = true)
    val searchSuggestionId: Int,
    val text: String
)