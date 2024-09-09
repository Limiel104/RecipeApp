package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient
import com.example.recipeapp.data.remote.ShoppingListDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.domain.model.ShoppingListWithIngredients
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ShoppingListMapperTest {

    private lateinit var shoppingList: ShoppingList
    private lateinit var shoppingListEntity: ShoppingListEntity
    private lateinit var shoppingListDto: ShoppingListDto
    private lateinit var shoppingListIngredientEntity: ShoppingListIngredientEntity
    private lateinit var shoppingListIngredientEntity2: ShoppingListIngredientEntity
    private lateinit var ingredientEntity: IngredientEntity
    private lateinit var ingredientEntity2: IngredientEntity
    private lateinit var ingredient: Ingredient
    private lateinit var ingredient2: Ingredient
    private lateinit var shoppingListWithIngredient: ShoppingListWithIngredient
    private lateinit var shoppingListWithIngredients: ShoppingListWithIngredients

    @Before
    fun setUp() {
        shoppingList = ShoppingList(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            date = 1234324354
        )

        shoppingListEntity = ShoppingListEntity(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            date = 1234324354
        )

        shoppingListDto = ShoppingListDto(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            ingredientMap = mapOf(
                "ingredientId" to "3 g",
                "ingredient2Id" to "5 g"
            ),
            checkedIngredientMap = mapOf(
                "ingredientId" to false,
                "ingredient2Id" to true
            ),
            date = 1234324354
        )

        shoppingListIngredientEntity = ShoppingListIngredientEntity(
            shoppingListIngredientId = 0,
            ingredientId = "ingredientId",
            shoppingListId = "shoppingListId",
            quantity = "3 g",
            isChecked = false
        )

        shoppingListIngredientEntity2 = ShoppingListIngredientEntity(
            shoppingListIngredientId = 0,
            ingredientId = "ingredient2Id",
            shoppingListId = "shoppingListId",
            quantity = "5 g",
            isChecked = true
        )

        ingredientEntity = IngredientEntity(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredientEntity2 = IngredientEntity(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient = Ingredient(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient2 = Ingredient(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        shoppingListWithIngredient = ShoppingListWithIngredient(
            shoppingList = shoppingListEntity,
            shoppingListIngredients = listOf(shoppingListIngredientEntity, shoppingListIngredientEntity2)
        )

        shoppingListWithIngredients = ShoppingListWithIngredients(
            shoppingListId = "shoppingListId",
            name = "Shopping List Name",
            createdBy = "userId",
            ingredients = mapOf(
                ingredient to "3 g",
                ingredient2 to "5 g"
            ),
            checkedIngredients = mapOf(
                ingredient to false,
                ingredient2 to true
            ),
            date = 1234324354
        )
    }

    @Test
    fun `ShoppingListEntity can be mapped to ShoppingList`() {
        val mappedShoppingList = shoppingListEntity.toShoppingList()

        assertThat(mappedShoppingList).isEqualTo(shoppingList)
    }

    @Test
    fun `ShoppingListDto can be mapped to ShoppingListEntity`() {
        val mappedShoppingListEntity = shoppingListDto.toShoppingListEntity()

        assertThat(mappedShoppingListEntity).isEqualTo(shoppingListEntity)
    }

    @Test
    fun `get list of ShoppingListIngredientEntity from ShoppingListDto`() {
        val mappedList = shoppingListDto.getShoppingListIngredientsEntityList()

        assertThat(mappedList).containsExactlyElementsIn(listOf(shoppingListIngredientEntity, shoppingListIngredientEntity2))
    }

    @Test
    fun `ShoppingListWithIngredient can be mapped to ShoppingListWithIngredients (relation to model)`() {
        val ingredients = listOf(ingredient, ingredient2)
        val mappedShoppingListWithIngredients = shoppingListWithIngredient.toShoppingListWithIngredients(ingredients)

        assertThat(mappedShoppingListWithIngredients).isEqualTo(shoppingListWithIngredients)
    }

    @Test
    fun `ShoppingListWithIngredients can be mapped to ShoppingListDto`() {
        val mappedShoppingListDto = shoppingListWithIngredients.toShoppingListDto("shoppingListId")

        assertThat(mappedShoppingListDto).isEqualTo(shoppingListDto)
    }
}