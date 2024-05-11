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
        val containsSpecialChar = name.let {
            val namePattern = "^(?=.*[@#!*$%^&+=])(?=\\S+$)$"
            val nameMatcher = Regex(namePattern)
            nameMatcher.find(name) != null
        }
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