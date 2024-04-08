package com.example.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [IngredientEntity::class],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val ingredientDao: IngredientDao

    companion object {
        const val DATABASE_NAME = "recipes.db"
    }
}