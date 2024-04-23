package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.RecipeDao
import com.example.recipeapp.data.mapper.getRecipeCategoryList
import com.example.recipeapp.data.mapper.getRecipeIngredientsList
import com.example.recipeapp.data.mapper.toIngredient
import com.example.recipeapp.data.mapper.toRecipe
import com.example.recipeapp.data.mapper.toRecipeDto
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
    override suspend fun addRecipe(recipeWithIngredients: RecipeWithIngredients) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        val documentId = recipesRef.document().id
        recipesRef.document(documentId).set(recipeWithIngredients.toRecipeDto(documentId)).await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getRecipe(recipeId: String) = flow<Resource<RecipeWithIngredients>> {
        emit(Resource.Loading(true))

        val recipeWithIngredient = dao.getRecipeWithIngredients(recipeId)
        val ingredients = dao.getIngredientsFromRecipe(recipeId).map { it.toIngredient() }
        val categories = dao.getCategoriesFromRecipe(recipeId)
        val recipeWithIngredients = recipeWithIngredient.toRecipeWithIngredients(ingredients, categories)
        emit(Resource.Success(recipeWithIngredients))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getRecipes(getRecipesFromRemote: Boolean, query: String, category: String) = flow<Resource<List<Recipe>>> {
        emit(Resource.Loading(true))

        val recipes = if(category.isEmpty())
            dao.getRecipes(query)
        else
            dao.getRecipesFromCategory(query, category)
        val loadFromCache = recipes.isNotEmpty() && !getRecipesFromRemote

        if(loadFromCache) {
            emit(Resource.Success(recipes.map { it.toRecipe() }))
            emit(Resource.Loading(false))
            Log.i("TAG","Recipes from cache")
            return@flow
        }

        val snapshot = recipesRef.get().await()
        val recipesFromRemote = snapshot.toObjects(RecipeDto::class.java)

        recipesFromRemote.let { recipeList ->
            dao.deleteAllRecipes()
            for(recipe in recipeList) {
                dao.insertRecipeWithIngredients(
                    recipe.toRecipeEntity(),
                    recipe.getRecipeIngredientsList(),
                    recipe.getRecipeCategoryList()
                )
            }
        }

        if(category.isEmpty())
            emit(Resource.Success(dao.getRecipes(query).map { it.toRecipe() }))
        else
            emit(Resource.Success(dao.getRecipesFromCategory(query, category).map { it.toRecipe() }))
        Log.i("TAG","Recipes from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteRecipe(recipeId: String) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        recipesRef.document(recipeId).delete().await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}