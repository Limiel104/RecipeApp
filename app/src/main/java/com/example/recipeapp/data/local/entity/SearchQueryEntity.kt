package com.example.recipeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val searchQueryId: Int,
    val query: String
)