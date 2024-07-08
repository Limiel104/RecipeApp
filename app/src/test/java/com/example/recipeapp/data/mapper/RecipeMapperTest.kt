package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeCategoryEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.relation.RecipeWithCategory
import com.example.recipeapp.data.local.relation.RecipeWithIngredient
import com.example.recipeapp.data.remote.RecipeDto
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RecipeMapperTest {

    private lateinit var recipeCategoryEntity: RecipeCategoryEntity
    private lateinit var recipeCategoryEntity2: RecipeCategoryEntity
    private lateinit var recipeCategoryEntity3: RecipeCategoryEntity
    private lateinit var recipeWithCategory: RecipeWithCategory
    private lateinit var recipe: Recipe
    private lateinit var recipeEntity: RecipeEntity
    private lateinit var recipeDto: RecipeDto
    private lateinit var recipeIngredientEntity: RecipeIngredientEntity
    private lateinit var recipeIngredientEntity2: RecipeIngredientEntity
    private lateinit var ingredientEntity: IngredientEntity
    private lateinit var ingredientEntity2: IngredientEntity
    private lateinit var ingredient: Ingredient
    private lateinit var ingredient2: Ingredient
    private lateinit var recipeWithIngredient: RecipeWithIngredient
    private lateinit var recipeWithIngredients: RecipeWithIngredients
    private lateinit var categoryList: List<RecipeCategoryEntity>

    @Before
    fun setUp() {
        recipeCategoryEntity = RecipeCategoryEntity(
            categoryId = 1,
            categoryName = "Category",
            recipeId = "recipeId"
        )

        recipeCategoryEntity2 = RecipeCategoryEntity(
            categoryId = 1,
            categoryName = "Category2",
            recipeId = "recipeId"
        )

        recipeCategoryEntity3 = RecipeCategoryEntity(
            categoryId = 1,
            categoryName = "Category3",
            recipeId = "recipeId"
        )

        recipeWithCategory = RecipeWithCategory(
            recipe = RecipeEntity(
                recipeId = "recipeId",
                name = "Recipe Name",
                prepTime = "40 min",
                servings = 4,
                description = "Recipe description",
                isVegetarian = true,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "userId"
            ),
            categories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
        )

        recipe = Recipe(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Category", "Category2", "Category3")
        )

        recipeEntity = RecipeEntity(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId"
        )

        recipeDto = RecipeDto(
            recipeId = "recipeId",
            name = "Recipe Name",
            ingredientMap = mapOf(
                "ingredientId" to "3 g",
                "ingredient2Id" to "5 g"
            ),
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categoryList = listOf("Category", "Category2", "Category3")
        )

        recipeIngredientEntity = RecipeIngredientEntity(
            recipeIngredientId = 0,
            ingredientId = "ingredientId",
            recipeId = "recipeId",
            quantity = "3 g"
        )

        recipeIngredientEntity2 = RecipeIngredientEntity(
            recipeIngredientId = 0,
            ingredientId = "ingredient2Id",
            recipeId = "recipeId",
            quantity = "5 g"
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

        recipeWithIngredient = RecipeWithIngredient(
            recipe = recipeEntity,
            recipeIngredients = listOf(recipeIngredientEntity, recipeIngredientEntity2)
        )

        recipeWithIngredients = RecipeWithIngredients(
            recipeId = "recipeId",
            name = "Recipe Name",
            ingredients = mapOf(
                ingredient to "3 g",
                ingredient2 to "5 g"
            ),
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            categories = listOf("Category", "Category2", "Category3")
        )

        categoryList = listOf(
            RecipeCategoryEntity(
                categoryId = 0,
                categoryName = "Category",
                recipeId = "recipeId"
            ),
            RecipeCategoryEntity(
                categoryId = 0,
                categoryName = "Category2",
                recipeId = "recipeId"
            ),
            RecipeCategoryEntity(
                categoryId = 0,
                categoryName = "Category3",
                recipeId = "recipeId"
            )
        )
    }

    @Test
    fun `RecipeEntity can be mapped to Recipe`() {
        val mappedRecipe = recipeWithCategory.toRecipe()

        assertThat(mappedRecipe).isEqualTo(recipe)
    }

    @Test
    fun `RecipeDto can be mapped to RecipeEntity`() {
        val mappedRecipeEntity = recipeDto.toRecipeEntity()

        assertThat(mappedRecipeEntity).isEqualTo(recipeEntity)
    }

    @Test
    fun `get list of RecipeIngredientEntity from RecipeDto`() {
        val mappedList = recipeDto.getRecipeIngredientsList()

        assertThat(mappedList).containsExactlyElementsIn(listOf(recipeIngredientEntity, recipeIngredientEntity2))
    }

    @Test
    fun `RecipeWithIngredient can be mapped to RecipeWithIngredients (relation to model)`() {
        val ingredients = listOf(ingredient, ingredient2)
        val categories = listOf("Category", "Category2", "Category3")
        val mappedRecipeWithIngredients = recipeWithIngredient.toRecipeWithIngredients(ingredients,categories)

        assertThat(mappedRecipeWithIngredients).isEqualTo(recipeWithIngredients)
    }

    @Test
    fun `RecipeWithIngredients can be mapped to RecipeDto`() {
        val mappedRecipeDto = recipeWithIngredients.toRecipeDto("recipeId")

        assertThat(mappedRecipeDto).isEqualTo(recipeDto)
    }

    @Test
    fun `get list of RecipeCategory from RecipeDto`() {
        val mappedList = recipeDto.getRecipeCategoryList()

        assertThat(mappedList).isEqualTo(categoryList)
    }

    @Test
    fun `RecipeWithCategory can be mapped to Recipe`() {
        val mappedRecipe = recipeWithCategory.toRecipe()

        assertThat(mappedRecipe).isEqualTo(recipe)
    }
}