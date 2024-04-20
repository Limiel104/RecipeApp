package com.example.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.local.entity.SearchQueryEntity
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
        SearchQueryEntity::class
    ],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao
    abstract val ingredientDao: IngredientDao
    abstract val shoppingListDao: ShoppingListDao
    abstract val savedRecipeDao: SavedRecipeDao
    abstract val searchQueryDao: SearchQueryDao

    companion object {
        const val DATABASE_NAME = "recipes.db"
    }
}