package com.example.recipeapp.di

import android.app.Application
import androidx.room.Room
import com.example.recipeapp.data.local.RecipeDatabase
import com.example.recipeapp.data.repository.CategoryRepositoryImpl
import com.example.recipeapp.data.repository.IngredientRepositoryImpl
import com.example.recipeapp.data.repository.RecipeRepositoryImpl
import com.example.recipeapp.data.repository.SavedRecipeRepositoryImpl
import com.example.recipeapp.data.repository.SearchSuggestionRepositoryImpl
import com.example.recipeapp.data.repository.ShoppingListRepositoryImpl
import com.example.recipeapp.domain.repository.CategoryRepository
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.repository.SearchSuggestionRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserShoppingListsUseCase
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideSearchSuggestionRepository(db: RecipeDatabase): SearchSuggestionRepository {
        return SearchSuggestionRepositoryImpl(db.searchSuggestionDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: RecipeDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideGetIngredientsUseCase(ingredientRepository: IngredientRepository): GetIngredientsUseCase {
        return GetIngredientsUseCase(ingredientRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecipesUseCase(recipeRepository: RecipeRepository): GetRecipesUseCase {
        return GetRecipesUseCase(recipeRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserShoppingListsUseCase(shoppingListRepository: ShoppingListRepository): GetUserShoppingListsUseCase {
        return GetUserShoppingListsUseCase(shoppingListRepository)
    }

    @Provides
    @Singleton
    fun provideAddSearchSuggestionUseCase(searchSuggestionRepository: SearchSuggestionRepository): AddSearchSuggestionUseCase {
        return AddSearchSuggestionUseCase(searchSuggestionRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchSuggestionsUseCase(searchSuggestionRepository: SearchSuggestionRepository): GetSearchSuggestionsUseCase {
        return GetSearchSuggestionsUseCase(searchSuggestionRepository)
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(categoryRepository: CategoryRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(categoryRepository)
    }
}