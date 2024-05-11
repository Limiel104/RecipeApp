package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class AddUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Flow<Resource<Boolean>> {
        return userRepository.addUser(user)
    }
}