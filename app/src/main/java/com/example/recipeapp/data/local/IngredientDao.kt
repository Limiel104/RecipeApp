package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredientDao {

    @Insert
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query(
        """
            SELECT *
            FROM ingrediententity
            WHERE id = :ingredientId
        """
    )
    suspend fun getIngredient(ingredientId: String): IngredientEntity

    @Query("SELECT * FROM ingrediententity")
    suspend fun getIngredients(): List<IngredientEntity>

    @Query("DELETE FROM ingrediententity")
    suspend fun deleteIngredients()
}