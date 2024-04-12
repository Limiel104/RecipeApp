package com.example.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity

@Database(
    entities = [IngredientEntity::class, RecipeEntity::class, IngredientQuantityEntity::class],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {
        const val DATABASE_NAME = "recipes.db"
    }
}