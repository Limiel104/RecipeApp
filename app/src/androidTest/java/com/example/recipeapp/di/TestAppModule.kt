package com.example.recipeapp.di

import android.app.Application
import androidx.room.Room
import com.example.recipeapp.data.local.RecipeDatabase
import com.example.recipeapp.data.repository.IngredientRepositoryImpl
import com.example.recipeapp.data.repository.RecipeRepositoryImpl
import com.example.recipeapp.data.repository.SavedRecipeRepositoryImpl
import com.example.recipeapp.data.repository.ShoppingListRepositoryImpl
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    fun provideRecipeDatabase(application: Application): RecipeDatabase {
        return Room.inMemoryDatabaseBuilder(
            application,
            RecipeDatabase::class.java
        ).build()
    }

    @Provides
    fun provideRecipeRepository(db: RecipeDatabase): RecipeRepository {
        val recipesRef = Firebase.firestore.collection("recipes")
        return RecipeRepositoryImpl(recipesRef, db.recipeDao)
    }

    @Provides
    fun provideIngredientRepository(db: RecipeDatabase): IngredientRepository {
        val ingredientsRef = Firebase.firestore.collection("ingredients")
        return IngredientRepositoryImpl(ingredientsRef, db.ingredientDao)
    }

    @Provides
    fun provideShoppingListRepository(db: RecipeDatabase): ShoppingListRepository {
        val shoppingListsRef = Firebase.firestore.collection("shoppingLists")
        return ShoppingListRepositoryImpl(shoppingListsRef, db.shoppingListDao)
    }

    @Provides
    fun provideSavedRecipeRepository(db: RecipeDatabase): SavedRecipeRepository {
        val savedRecipesRef = Firebase.firestore.collection("savedRecipes")
        return SavedRecipeRepositoryImpl(savedRecipesRef, db.savedRecipeDao)
    }
}