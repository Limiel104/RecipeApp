package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateFieldUseCase {

    operator fun invoke(fieldText: String): ValidationResult {
        if (fieldText.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Field can't be empty"
            )
        }
        if(fieldText.length < 4) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Field is too short"
            )
        }
        val containsSpecialChar = fieldText.any { it in "@#*$%^&_+={}[];<>/" }
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