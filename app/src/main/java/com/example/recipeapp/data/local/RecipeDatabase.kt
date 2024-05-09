package com.example.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.entity.CategoryEntity
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeCategoryEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.local.entity.SearchSuggestionEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity

@Database(
    entities = [
        IngredientEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        ShoppingListEntity::class,
        ShoppingListIngredientEntity::class,
        SavedRecipeEntity::class,
        SearchSuggestionEntity::class,
        RecipeCategoryEntity::class,
        CategoryEntity::class
    ],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao
    abstract val ingredientDao: IngredientDao
    abstract val shoppingListDao: ShoppingListDao
    abstract val savedRecipeDao: SavedRecipeDao
    abstract val searchSuggestionDao: SearchSuggestionDao
    abstract val categoryDao: CategoryDao

    companion object {
        const val DATABASE_NAME = "recipes.db"
    }
}