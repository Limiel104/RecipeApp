package com.example.recipeapp.di

import android.app.Application
import androidx.room.Room
import com.example.recipeapp.data.local.RecipeDatabase
import com.example.recipeapp.data.repository.IngredientRepositoryImpl
import com.example.recipeapp.data.repository.RecipeRepositoryImpl
import com.example.recipeapp.data.repository.SavedRecipeRepositoryImpl
import com.example.recipeapp.data.repository.SearchQueryRepositoryImpl
import com.example.recipeapp.data.repository.ShoppingListRepositoryImpl
import com.example.recipeapp.domain.repository.IngredientRepository
import com.example.recipeapp.domain.repository.RecipeRepository
import com.example.recipeapp.domain.repository.SavedRecipeRepository
import com.example.recipeapp.domain.repository.SearchQueryRepository
import com.example.recipeapp.domain.repository.ShoppingListRepository
import com.example.recipeapp.domain.use_case.AddSearchQueryUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.GetRecentSearchQueriesUseCase
import com.example.recipeapp.domain.use_case.GetRecipesUseCase
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
object AppModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(application: Application): RecipeDatabase {
        return Room.databaseBuilder(
            application,
            RecipeDatabase::class.java,
            RecipeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(db: RecipeDatabase): RecipeRepository {
        val recipesRef = Firebase.firestore.collection("recipes")
        return RecipeRepositoryImpl(recipesRef, db.recipeDao)
    }

    @Provides
    @Singleton
    fun provideIngredientRepository(db: RecipeDatabase): IngredientRepository {
        val ingredientsRef = Firebase.firestore.collection("ingredients")
        return IngredientRepositoryImpl(ingredientsRef, db.ingredientDao)
    }

    @Provides
    @Singleton
    fun provideShoppingListRepository(db: RecipeDatabase): ShoppingListRepository {
        val shoppingListsRef = Firebase.firestore.collection("shoppingLists")
        return ShoppingListRepositoryImpl(shoppingListsRef, db.shoppingListDao)
    }

    @Provides
    @Singleton
    fun provideSavedRecipeRepository(db: RecipeDatabase): SavedRecipeRepository {
        val savedRecipesRef = Firebase.firestore.collection("savedRecipes")
        return SavedRecipeRepositoryImpl(savedRecipesRef, db.savedRecipeDao)
    }

    @Provides
    @Singleton
    fun provideSearchQueryRepository(db: RecipeDatabase): SearchQueryRepository {
        return SearchQueryRepositoryImpl(db.searchQueryDao)
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
    fun provideAddSearchQueryUseCase(searchQueryRepository: SearchQueryRepository): AddSearchQueryUseCase {
        return AddSearchQueryUseCase(searchQueryRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecentSearchQueriesUseCase(searchQueryRepository: SearchQueryRepository): GetRecentSearchQueriesUseCase {
        return GetRecentSearchQueriesUseCase(searchQueryRepository)
    }
}