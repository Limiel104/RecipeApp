package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredientsQuantity

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert
    suspend fun ingredientsQuantity(ingredientsQuantity: List<IngredientQuantityEntity>)

    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity,ingredients: List<IngredientEntity>) {
        insertRecipe(recipe)
        insertIngredients(ingredients)
    }

    @Transaction
    suspend fun insertRecipeWithIngredientsQuantity(recipe: RecipeEntity, ingredientsQuantity: List<IngredientQuantityEntity>) {
        insertRecipe(recipe)
        ingredientsQuantity(ingredientsQuantity)
    }

    @Transaction
    @Query(
        """
            SELECT *
            FROM recipeentity
            WHERE recipeId = :recipeId
        """
    )
    suspend fun getRecipeWithIngredientsQuantity(recipeId: String): RecipeWithIngredientsQuantity

    @Transaction
    @Query("SELECT * FROM recipeentity")
    suspend fun getRecipesWithIngredientsQuantity(): List<RecipeWithIngredientsQuantity>

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

    @Query(
        """
            SELECT ingrediententity.ingredientId, ingrediententity.category, ingrediententity.name, ingrediententity.imageUrl
            FROM ingrediententity
            JOIN ingredientquantityentity ON ingredientquantityentity.ingredientId = ingrediententity.ingredientId
            WHERE ingredientquantityentity.recipeId = :recipeId
        """
    )
    suspend fun getIngredientsFromRecipe(recipeId: String): List<IngredientEntity>

    @Query("DELETE FROM recipeentity")
    suspend fun deleteRecipes()

    @Query("DELETE FROM ingrediententity")
    suspend fun deleteIngredients()
}