package com.example.recipeapp.data.repository

import android.net.Uri
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.ImageStorageRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageStorageRepositoryImpl @Inject constructor(
    private val imagesStorageRef: StorageReference
): ImageStorageRepository {
    override suspend fun addImage(imageUri: Uri, imageName: String)  = flow<Resource<Uri>> {
        emit(Resource.Loading(true))

        val imageUrl = imagesStorageRef.child(imageName).putFile(imageUri).await().storage.downloadUrl.await()
        emit(Resource.Success(imageUrl))

        emit(Resource.Loading(false))

    }.catch {
        emit(Resource.Error(it.localizedMessage as String))
    }.flowOn(Dispatchers.IO)
}