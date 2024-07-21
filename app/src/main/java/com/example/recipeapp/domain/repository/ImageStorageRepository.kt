package com.example.recipeapp.domain.repository

import android.net.Uri
import com.example.recipeapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ImageStorageRepository {
    suspend fun addImage(imageUri: Uri, imageName: String): Flow<Resource<Uri>>
}