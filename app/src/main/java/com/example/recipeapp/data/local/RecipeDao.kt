package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredient

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert
    suspend fun insertRecipeIngredients(recipeIngredients: List<RecipeIngredientEntity>)

    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity, recipeIngredients: List<RecipeIngredientEntity>) {
        insertRecipe(recipe)
        insertRecipeIngredients(recipeIngredients)
    }

    @Query("SELECT * FROM recipeentity")
    suspend fun getRecipes(): List<RecipeEntity>

    @Transaction
    @Query(
        """
            SELECT *
            FROM recipeentity
            WHERE recipeId = :recipeId
        """
    )
    suspend fun getRecipeWithIngredients(recipeId: String): RecipeWithIngredient

    @Query(
        """
            SELECT * 
            FROM recipeentity 
            WHERE createdBy = :userId
        """
    )
    fun getUserRecipes(userId: String): List<RecipeEntity>

    @Query(
        """
            SELECT ingrediententity.ingredientId, ingrediententity.category, ingrediententity.name, ingrediententity.imageUrl
            FROM ingrediententity
            JOIN recipeingrediententity ON recipeingrediententity.ingredientId = ingrediententity.ingredientId
            WHERE recipeingrediententity.recipeId = :recipeId
        """
    )
    suspend fun getIngredientsFromRecipe(recipeId: String): List<IngredientEntity>

    @Query("DELETE FROM recipeentity")
    suspend fun deleteRecipes()
}