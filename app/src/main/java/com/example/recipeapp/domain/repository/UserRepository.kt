package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(user: User): Flow<Resource<Boolean>>
    suspend fun getUser(userUID: String): Flow<Resource<User>>
    suspend fun updateUser(user: User): Flow<Resource<Boolean>>
}