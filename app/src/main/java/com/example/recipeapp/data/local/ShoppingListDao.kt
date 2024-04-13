package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient

@Dao
interface ShoppingListDao {

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity)

    @Insert
    suspend fun insertShoppingListIngredients(shoppingListIngredients: List<ShoppingListIngredientEntity>)

    @Transaction
    suspend fun insertShoppingListWithIngredients(shoppingList: ShoppingListEntity, shoppingListIngredients: List<ShoppingListIngredientEntity>) {
        insertShoppingList(shoppingList)
        insertShoppingListIngredients(shoppingListIngredients)
    }

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
    suspend fun getShoppingListWithIngredients(shoppingListId: String): ShoppingListWithIngredient

    @Query(
        """
            SELECT ingrediententity.ingredientId, ingrediententity.category, ingrediententity.name, ingrediententity.imageUrl
            FROM ingrediententity
            JOIN shoppinglistingrediententity ON shoppinglistingrediententity.ingredientId = ingrediententity.ingredientId
            WHERE shoppinglistingrediententity.shoppingListId = :shoppingListId
        """
    )
    suspend fun getIngredientsFromShoppingList(shoppingListId: String): List<IngredientEntity>

    @Query("DELETE FROM shoppinglistentity")
    suspend fun deleteShoppingLists()
}