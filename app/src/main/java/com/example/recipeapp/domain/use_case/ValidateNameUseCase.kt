package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateNameUseCase {
    operator fun invoke(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Name can't be empty"
            )
        }
        if(name.length < 8) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Name is too short"
            )
        }
        val containsSpecialChar = name.any { it in "@#!*$%^&_+=-{}[]:;<>,.?/" }
        if (containsSpecialChar) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "At least one character in name is not allowed"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}