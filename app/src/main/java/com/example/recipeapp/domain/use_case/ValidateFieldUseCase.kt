package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateFieldUseCase {

    operator fun invoke(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Field can't be empty"
            )
        }
        if(name.length < 4) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Field is too short"
            )
        }
        val containsSpecialChar = name.any { it in "@#*$%^&_+={}[];<>/" }
        if (containsSpecialChar) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "At least one character is not allowed"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}