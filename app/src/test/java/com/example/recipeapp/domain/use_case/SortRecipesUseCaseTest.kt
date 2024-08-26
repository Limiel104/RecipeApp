package com.example.recipeapp.domain.use_case

import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.util.RecipeOrder
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SortRecipesUseCaseTest {

    private lateinit var sortRecipesUseCase: SortRecipesUseCase
    private lateinit var recipes: List<Recipe>

    @Before
    fun setUp() {
        sortRecipesUseCase = SortRecipesUseCase()

        recipes = listOf(
            Recipe(
                recipeId = "recipe2Id",
                name = "Recipe 2 Name",
                prepTime = "25 min",
                servings = 1,
                description = "Recipe 2 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image2Url",
                createdBy = "userId",
                categories = listOf("Category", "Category3"),
                date = 1234567891
            ),
            Recipe(
                recipeId = "recipe5Id",
                name = "Recipe 5 Name",
                prepTime = "15 min",
                servings = 1,
                description = "Recipe 5 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image5Url",
                createdBy = "userId",
                categories = listOf("Category1"),
                date = 1234567894
            ),
            Recipe(
                recipeId = "recipe4Id",
                name = "Recipe 4 Name",
                prepTime = "1 h 15 min",
                servings = 4,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image4Url",
                createdBy = "userId",
                categories = listOf("Category4","Category1","Category2"),
                date = 1234567893
            ),
            Recipe(
                recipeId = "recipeId",
                name = "Recipe Name",
                prepTime = "40 min",
                servings = 4,
                description = "Recipe description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "userId",
                categories = listOf("Category", "Category2", "Category3"),
                date = 1234567890
            ),
            Recipe(
                recipeId = "recipe6Id",
                name = "Recipe 6 Name",
                prepTime = "30 min",
                servings = 2,
                description = "Recipe 6 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image6Url",
                createdBy = "userId",
                categories = listOf("Category2"),
                date = 1234567895
            ),
            Recipe(
                recipeId = "recipe3Id",
                name = "Recipe 3 Name",
                prepTime = "1 h",
                servings = 6,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image3Url",
                createdBy = "userId",
                categories = listOf("Category4"),
                date = 1234567892
            )
        )
    }

    @Test
    fun `recipes are sorted in descending order`() {
        val result = sortRecipesUseCase(RecipeOrder.DateDescending, recipes)

        assertThat(result).isEqualTo(recipes.sortedByDescending { it.date })
    }

    @Test
    fun `recipes are sorted in ascending order`() {
        val result = sortRecipesUseCase(RecipeOrder.DateAscending, recipes)

        assertThat(result).isEqualTo(recipes.sortedBy { it.date })
    }
}