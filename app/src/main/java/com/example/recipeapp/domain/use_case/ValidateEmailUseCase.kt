package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Email can't be empty"
            )
        }
        val containsSpecialCharacters = email.any { it in "#!*$%^&_+=-_{}[]:;<>,?/" }
        if(containsSpecialCharacters) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Email in wrong format"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}