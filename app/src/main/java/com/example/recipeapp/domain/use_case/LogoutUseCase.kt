package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        return authRepository.logout()
    }
}