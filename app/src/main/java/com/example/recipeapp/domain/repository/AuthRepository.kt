package com.example.recipeapp.domain.repository

import com.example.recipeapp.domain.model.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun signup(email: String, password: String): Flow<Resource<FirebaseUser>>
    fun logout()
}