package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.RecipeDao
import com.example.recipeapp.data.mapper.toIngredient
import com.example.recipeapp.data.mapper.toIngredientEntity
import com.example.recipeapp.data.remote.IngredientDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.util.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class IngredientRepositoryImpl @Inject constructor(
    private val ingredientsRef: CollectionReference,
    private val dao: RecipeDao
): IngredientRepository {
    override suspend fun getIngredient(ingredientId: String) = flow<Resource<Ingredient>> {
        emit(Resource.Loading(true))

        val ingredient = dao.getIngredient(ingredientId)
        emit(Resource.Success(ingredient.toIngredient()))

        emit(Resource.Loading(false))
    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getIngredients() = flow<Resource<List<Ingredient>>> {
        emit(Resource.Loading(true))

        val ingredients = dao.getIngredients()
        val loadFromCache = ingredients.isNotEmpty()

        if(loadFromCache) {
            emit(Resource.Success(ingredients.map { it.toIngredient() }))
            emit(Resource.Loading(false))
            Log.i("TAG","Ingredients from cache")
            return@flow
        }

        val snapshot = ingredientsRef.get().await()
        val ingredientsFromRemote = snapshot.toObjects(IngredientDto::class.java)

        ingredientsFromRemote.let { ingredientList ->
            dao.deleteIngredients()
            dao.insertIngredients(ingredientList.map { it.toIngredientEntity() })
        }

        emit(Resource.Success(ingredientsFromRemote.map { it.toIngredient() }))
        Log.i("TAG","Ingredients from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}