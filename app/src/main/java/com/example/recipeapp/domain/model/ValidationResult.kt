package com.example.recipeapp.domain.model

data class ValidationResult(
    val isSuccessful: Boolean,
    val errorMessage: String? = null
)