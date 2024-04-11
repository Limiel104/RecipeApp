package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.RecipeDao
import com.example.recipeapp.data.mapper.getIngredientsQuantityList
import com.example.recipeapp.data.mapper.toRecipe
import com.example.recipeapp.data.mapper.toRecipeEntity
import com.example.recipeapp.data.remote.RecipeDto
import com.example.recipeapp.domain.model.Recipe
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
    override suspend fun getRecipe(recipeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipes() = flow<Resource<List<Recipe>>> {
        emit(Resource.Loading(true))

        val recipes = dao.getRecipesWithIngredientsQuantity()
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
                dao.insertRecipeWithIngredientsQuantity(
                    recipe.toRecipeEntity(),
                    recipe.getIngredientsQuantityList()
                )
            }
        }

        emit(Resource.Success(dao.getRecipesWithIngredientsQuantity().map { it.toRecipe() }))
        Log.i("TAG","Recipes from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}