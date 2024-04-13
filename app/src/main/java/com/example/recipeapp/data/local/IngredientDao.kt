package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.data.local.entity.IngredientEntity

@Dao
interface IngredientDao {

    @Insert
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query(
        """
            SELECT *
            FROM ingrediententity
            WHERE ingredientId = :ingredientId
        """
    )
    suspend fun getIngredient(ingredientId: String): IngredientEntity

    @Query("SELECT * FROM ingrediententity")
    suspend fun getIngredients(): List<IngredientEntity>

    @Query("DELETE FROM ingrediententity")
    suspend fun deleteIngredients()
}