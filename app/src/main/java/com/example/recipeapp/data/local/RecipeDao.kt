package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredients

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)
    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity,ingredients: List<IngredientEntity>) {
        insertRecipe(recipe)
        insertIngredients(ingredients)
    }

    @Transaction
    @Query(
        """
            SELECT *
            FROM recipeentity
            WHERE recipeId = :recipeId
        """
    )
    suspend fun getRecipe(recipeId: String): RecipeWithIngredients?

    @Transaction
    @Query("SELECT * FROM recipeentity")
    suspend fun getRecipes(): List<RecipeWithIngredients>

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
    @Query("DELETE FROM recipeentity")
    suspend fun deleteRecipes()

    @Query("DELETE FROM ingrediententity")
    suspend fun deleteIngredients()
}