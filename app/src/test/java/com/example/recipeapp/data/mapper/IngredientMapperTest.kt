package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.remote.IngredientDto
import com.example.recipeapp.domain.model.Ingredient
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class IngredientMapperTest {

    private lateinit var ingredient: Ingredient
    private lateinit var ingredientEntity: IngredientEntity
    private lateinit var ingredientDto: IngredientDto

    @Before
    fun setUp() {

        ingredient = Ingredient(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredientEntity = IngredientEntity(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredientDto = IngredientDto(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )
    }

    @Test
    fun `IngredientEntity can be mapped to Ingredient`() {
        val mappedIngredient = ingredientEntity.toIngredient()

        assertThat(mappedIngredient).isEqualTo(ingredient)
    }

    @Test
    fun `IngredientDto can be mapped to IngredientEntity`() {
        val mappedIngredientEntity = ingredientDto.toIngredientEntity()

        assertThat(mappedIngredientEntity).isEqualTo(ingredientEntity)
    }

    @Test
    fun `IngredientDto can be mapped to Ingredient`() {
        val mappedIngredient = ingredientDto.toIngredient()

        assertThat(mappedIngredient).isEqualTo(ingredient)
    }
}