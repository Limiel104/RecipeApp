package com.example.recipeapp.data.repository

import android.util.Log
import com.example.recipeapp.data.local.ShoppingListDao
import com.example.recipeapp.data.mapper.getShoppingListIngredientsList
import com.example.recipeapp.data.mapper.toIngredient
import com.example.recipeapp.data.mapper.toShoppingList
import com.example.recipeapp.data.mapper.toShoppingListDto
import com.example.recipeapp.data.mapper.toShoppingListEntity
import com.example.recipeapp.data.mapper.toShoppingListWithIngredients
import com.example.recipeapp.data.remote.ShoppingListDto
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.util.Resource
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListsRef: CollectionReference,
    private val dao: ShoppingListDao
): ShoppingListRepository {
    override suspend fun addShoppingList(shoppingListWithIngredients: ShoppingListWithIngredients) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        val documentId = shoppingListsRef.document().id
        shoppingListsRef.document(documentId).set(shoppingListWithIngredients.toShoppingListDto(documentId)).await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getShoppingList(shoppingListId: String) = flow<Resource<ShoppingListWithIngredients>> {
        emit(Resource.Loading(true))

        val shoppingListWithIngredient = dao.getShoppingListWithIngredients(shoppingListId)
        val ingredients = dao.getIngredientsFromShoppingList(shoppingListId).map { it.toIngredient() }
        val shoppingListWithIngredients = shoppingListWithIngredient.toShoppingListWithIngredients(ingredients)
        emit(Resource.Success(shoppingListWithIngredients))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun getUserShoppingLists(userId: String, getShoppingListFromRepository: Boolean) = flow<Resource<List<ShoppingList>>> {
        emit(Resource.Loading(true))

        val shoppingLists = dao.getShoppingLists()
        val loadFromCache = shoppingLists.isNotEmpty() && !getShoppingListFromRepository

        if(loadFromCache) {
            emit(Resource.Success(shoppingLists.map { it.toShoppingList() }))
            emit(Resource.Loading(false))
            Log.i("TAG","Shopping Lists from cache")
            return@flow
        }

        val snapshot = shoppingListsRef.whereEqualTo("createdBy", userId).get().await()
        val shoppingListsFromRemote = snapshot.toObjects(ShoppingListDto::class.java)

        shoppingListsFromRemote.let { shoppingListsList ->
            dao.deleteShoppingListsWithIngredients()
            for(shoppingList in shoppingListsList) {
                dao.insertShoppingListWithIngredients(
                    shoppingList.toShoppingListEntity(),
                    shoppingList.getShoppingListIngredientsList()
                )
            }
        }

        emit(Resource.Success(dao.getShoppingLists().map { it.toShoppingList() }))
        Log.i("TAG","Shopping Lists from remote")
        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteShoppingList(shoppingListId: String) = flow<Resource<Boolean>> {
        emit(Resource.Loading(true))

        shoppingListsRef.document(shoppingListId).delete().await()
        emit(Resource.Success(true))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}