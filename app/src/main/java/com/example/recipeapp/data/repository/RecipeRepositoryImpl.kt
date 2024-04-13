package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.RecipeDao
import com.example.recipeapp.data.mapper.getRecipeIngredientsList
import com.example.recipeapp.data.mapper.toIngredient
import com.example.recipeapp.data.mapper.toRecipe
import com.example.recipeapp.data.mapper.toRecipeEntity
import com.example.recipeapp.data.mapper.toRecipeWithIngredients
import com.example.recipeapp.data.remote.RecipeDto
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.util.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipesRef: CollectionReference,
    private val dao: RecipeDao
): RecipeRepository {
    override suspend fun getRecipe(recipeId: String) = flow<Resource<RecipeWithIngredients>> {
        emit(Resource.Loading(true))

        val recipeWithIngredient = dao.getRecipeWithIngredients(recipeId)
        val ingredients = dao.getIngredientsFromRecipe(recipeId).map { it.toIngredient() }
        val recipeWithIngredients = recipeWithIngredient.toRecipeWithIngredients(ingredients)
        emit(Resource.Success(recipeWithIngredients))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getRecipes() = flow<Resource<List<Recipe>>> {
        emit(Resource.Loading(true))

        val recipes = dao.getRecipes()
        val loadFromCache = recipes.isNotEmpty()

        if(loadFromCache) {
            emit(Resource.Success(recipes.map { it.toRecipe() }))
            emit(Resource.Loading(false))
            Log.i("TAG","Recipes from cache")
            return@flow
        }

        val snapshot = recipesRef.get().await()
        val recipesFromRemote = snapshot.toObjects(RecipeDto::class.java)

        recipesFromRemote.let { recipeList ->
            dao.deleteRecipes()
            for(recipe in recipeList) {
                dao.insertRecipeWithIngredients(
                    recipe.toRecipeEntity(),
                    recipe.getRecipeIngredientsList()
                )
            }
        }

        emit(Resource.Success(dao.getRecipes().map { it.toRecipe() }))
        Log.i("TAG","Recipes from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}