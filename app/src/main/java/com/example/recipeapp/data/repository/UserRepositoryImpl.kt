package com.example.recipeapp.data.repository

import com.example.recipeapp.data.mapper.toUser
import com.example.recipeapp.data.mapper.toUserDto
import com.example.recipeapp.data.remote.UserDto
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.repository.UserRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val usersRef: CollectionReference
): UserRepository {
    override suspend fun addUser(user: User) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        usersRef.document(user.userUID).set(user.toUserDto()).await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getUser(userUID: String) = flow<Resource<User>> {
        emit(Resource.Loading(true))

        val snapshot = usersRef.whereEqualTo("userUID", userUID).get().await()
        val user = snapshot.toObjects(UserDto::class.java)
        emit(Resource.Success(user[0].toUser()))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUser(user: User) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        usersRef.document(user.userUID).update(
            mapOf(
                "userUID" to user.userUID,
                "name" to user.name
            )
        ).await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}