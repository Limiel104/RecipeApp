package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.IngredientQuantityEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredientsQuantity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity)

    @Insert
    suspend fun insertIngredientsQuantity(ingredientsQuantity: List<IngredientQuantityEntity>)

    @Insert
    suspend fun insertShoppingListIngredients(shoppingListIngredients: List<ShoppingListIngredientEntity>)

    @Transaction
    suspend fun insertRecipeWithIngredients(recipe: RecipeEntity,ingredients: List<IngredientEntity>) {
        insertRecipe(recipe)
        insertIngredients(ingredients)
    }

    @Transaction
    suspend fun insertRecipeWithIngredientsQuantity(recipe: RecipeEntity, ingredientsQuantity: List<IngredientQuantityEntity>) {
        insertRecipe(recipe)
        insertIngredientsQuantity(ingredientsQuantity)
    }

    @Transaction
    suspend fun insertShoppingListWithIngredients(shoppingList: ShoppingListEntity, shoppingListIngredients: List<ShoppingListIngredientEntity>) {
        insertShoppingList(shoppingList)
        insertShoppingListIngredients(shoppingListIngredients)
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
    suspend fun getRecipeWithIngredientsQuantity(recipeId: String): RecipeWithIngredientsQuantity

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

    @Query("SELECT * FROM shoppinglistentity")
    suspend fun getShoppingLists(): List<ShoppingListEntity>

    @Transaction
    @Query(
        """
            SELECT *
            FROM shoppinglistentity
            WHERE shoppingListId = :shoppingListId
        """
    )
    suspend fun getShoppingListWithIngredientsQuantity(shoppingListId: String): ShoppingListWithIngredient

    @Query(
        """
            SELECT ingrediententity.ingredientId, ingrediententity.category, ingrediententity.name, ingrediententity.imageUrl
            FROM ingrediententity
            JOIN shoppinglistingrediententity ON shoppinglistingrediententity.ingredientId = ingrediententity.ingredientId
            WHERE shoppinglistingrediententity.shoppingListId = :shoppingListId
        """
    )
    suspend fun getIngredientsFromShoppingList(shoppingListId: String): List<IngredientEntity>

    @Query("DELETE FROM recipeentity")
    suspend fun deleteRecipes()

    @Query("DELETE FROM ingrediententity")
    suspend fun deleteIngredients()

    @Query("DELETE FROM shoppinglistentity")
    suspend fun deleteShoppingLists()
}