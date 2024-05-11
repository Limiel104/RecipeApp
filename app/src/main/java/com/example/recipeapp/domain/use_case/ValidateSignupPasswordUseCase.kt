package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.ValidationResult

class ValidateSignupPasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        if(password.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password can't be empty"
            )
        }
        if(password.length < 8) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password is too short"
            )
        }
        val containsAtLeastOneDigit = password.any { it.isDigit() }
        if(!containsAtLeastOneDigit) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password should have at least one digit"
            )
        }
        val containsAtLeastOneCapitalLetter = password.any { it.isUpperCase() }
        if(!containsAtLeastOneCapitalLetter) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password should have at least one capital letter"
            )
        }
        val containsAtLeastOneSpecialChar = password.let {
            val passwordPattern = "^(?=.*[@#!*$%^&_+=])(?=\\S+$)$"
            val passwordMatcher = Regex(passwordPattern)
            passwordMatcher.find(password) != null
        }
        if(!containsAtLeastOneSpecialChar) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password should have at least one special character"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}