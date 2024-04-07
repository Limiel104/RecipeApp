package com.example.recipeapp.di

import com.example.recipeapp.data.repository.IngredientRepositoryImpl
import com.example.recipeapp.domain.repository.IngredientRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIngredientRepository(): IngredientRepository {
        val ingredientsRef = Firebase.firestore.collection("ingredients")
        return IngredientRepositoryImpl(ingredientsRef)
    }
}