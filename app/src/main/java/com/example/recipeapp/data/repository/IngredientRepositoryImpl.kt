package com.example.recipeapp.data.repository

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
    private val ingredientsRef: CollectionReference
): IngredientRepository {
//    override fun getIngredient(ingredientId: String) = callbackFlow {
//        val snapshotListener = ingredientsRef
//            .whereEqualTo("ingredientId", ingredientId)
//            .addSnapshotListener { snapshot, e ->
//                val response = if (snapshot != null) {
//                    val ingredient = snapshot.toObjects(Ingredient::class.java)
//                    Resource.Success(ingredient)
//                } else {
//                    Resource.Error(e!!.localizedMessage as String)
//                }
//                trySend(response)
//            }
//
//        awaitClose {
//            snapshotListener.remove()
//        }
//    }

    override suspend fun getIngredient(ingredientId: String) = flow<Resource<List<Ingredient>>> {
        emit(Resource.Loading())
        val snapshot = ingredientsRef.get().await()
        val ingredient = snapshot.toObjects(Ingredient::class.java)
        emit(Resource.Success(ingredient))
        emit(Resource.Loading(false))
    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}