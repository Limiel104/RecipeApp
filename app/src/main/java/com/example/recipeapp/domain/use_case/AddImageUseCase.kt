package com.example.recipeapp.domain.use_case

import android.net.Uri
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.repository.ImageStorageRepository
import kotlinx.coroutines.flow.Flow

class AddImageUseCase(
    private val imageStorageRepository: ImageStorageRepository
) {
    suspend operator fun invoke(imageUri: Uri, imageName: String): Flow<Resource<Uri>> {
        return imageStorageRepository.addImage(imageUri, imageName)
    }
}