package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.SavedRecipeDao
import com.example.recipeapp.data.mapper.toRecipe
import com.example.recipeapp.data.mapper.toSavedRecipeEntity
import com.example.recipeapp.data.remote.SavedRecipeDto
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.model.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SavedRecipeRepositoryImpl @Inject constructor(
    private val savedRecipeRef: CollectionReference,
    private val dao: SavedRecipeDao
): SavedRecipeRepository {
    override suspend fun addSavedRecipe(userId: String, recipeId: String) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        val documentId = savedRecipeRef.document().id
        savedRecipeRef.document(documentId).set(
            SavedRecipeDto(
                savedRecipeId = documentId,
                recipeId = recipeId,
                userId = userId
            )
        ).await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))
    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserSavedRecipes(userId: String, query: String, getSavedRecipesFromRemote: Boolean) = flow<Resource<List<Recipe>>> {
        emit(Resource.Loading(true))

        val savedRecipes = dao.getSavedRecipes(query)
        val loadFromCache = savedRecipes.isNotEmpty() && !getSavedRecipesFromRemote

        if(loadFromCache) {
            emit(Resource.Success(savedRecipes.map { it.toRecipe() }))
            emit(Resource.Loading(false))
            Log.i("TAG","Saved Recipes from cache")
            return@flow
        }

        val snapshot = savedRecipeRef.whereEqualTo("userId", userId).get().await()
        val savedRecipesFromRemote = snapshot.toObjects(SavedRecipeDto::class.java)

        savedRecipesFromRemote.let { savedRecipeList ->
            dao.deleteSavedRecipes()
            dao.insertSavedRecipes(savedRecipeList.map { it.toSavedRecipeEntity() })
        }

        emit(Resource.Success(dao.getSavedRecipes(query).map { it.toRecipe() }))
        Log.i("TAG","Saved Recipes from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getSavedRecipeId(userId: String, recipeId: String) = flow<Resource<String>> {
        emit(Resource.Loading(true))

        val savedRecipeId = dao.getSavedRecipeId(userId, recipeId).savedRecipeId
        emit(Resource.Success(savedRecipeId))

        emit(Resource.Loading(false))
    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteSavedRecipe(savedRecipeId: String) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        savedRecipeRef.document(savedRecipeId).delete().await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}