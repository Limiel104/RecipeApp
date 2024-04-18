package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.remote.SavedRecipeDto
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SavedRecipeMapperTest {

    private lateinit var savedRecipeEntity: SavedRecipeEntity
    private lateinit var savedRecipeDto: SavedRecipeDto

    @Before
    fun setUp() {
        savedRecipeEntity = SavedRecipeEntity(
            savedRecipeId = "savedRecipeId",
            recipeId = "recipeId",
            userId = "userId"
        )

        savedRecipeDto = SavedRecipeDto(
            savedRecipeId = "savedRecipeId",
            recipeId = "recipeId",
            userId = "userId"
        )
    }

    @Test
    fun `SavedRecipeDto can be mapped to SavedRecipeEntity`() {
        val mappedSavedRecipeEntity = savedRecipeDto.toSavedRecipeEntity()

        assertThat(mappedSavedRecipeEntity).isEqualTo(savedRecipeEntity)
    }
}