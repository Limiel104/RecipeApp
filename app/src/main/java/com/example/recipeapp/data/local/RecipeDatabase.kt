package com.example.recipeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity

@Database(
    entities = [
        IngredientEntity::class,
        RecipeEntity::class,
        IngredientQuantityEntity::class,
        ShoppingListEntity::class,
        ShoppingListIngredientEntity::class],
    version = 1
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val recipeDao: RecipeDao

    companion object {
        const val DATABASE_NAME = "recipes.db"
    }
}