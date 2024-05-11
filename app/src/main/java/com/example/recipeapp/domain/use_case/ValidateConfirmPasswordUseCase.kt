package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Passwords don't mach"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}