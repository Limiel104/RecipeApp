package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class UpdateUserPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(password: String): Flow<Resource<Boolean>> {
        return authRepository.updateUserPassword(password)
    }
}