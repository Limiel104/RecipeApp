package com.example.recipeapp.data.repository

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String) = flow<Resource<FirebaseUser>> {

        emit(Resource.Loading(true))

        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        emit(Resource.Success(result.user))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
        emit(Resource.Loading(false))
    }.flowOn(Dispatchers.IO)

    override suspend fun signup(email: String, password: String) = flow<Resource<FirebaseUser>> {

        emit(Resource.Loading(true))

        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        emit(Resource.Success(result.user))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
        emit(Resource.Loading(false))
    }.flowOn(Dispatchers.IO)

    override fun logout() {
        firebaseAuth.signOut()
    }
}