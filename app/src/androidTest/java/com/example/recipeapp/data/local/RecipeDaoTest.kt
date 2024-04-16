package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.relation.RecipeWithIngredient
import com.example.recipeapp.di.AppModule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class RecipeDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    private lateinit var dao: RecipeDao
    private lateinit var ingredientDao: IngredientDao

    private val recipe = RecipeEntity(
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

    private val recipe2 = RecipeEntity(
        recipeId = "recipe2Id",
        name = "Recipe 2 Name",
        prepTime = "1 h",
        servings = 2,
        description = "Recipe 2 description",
        isVegetarian = false,
        isVegan = false,
        imageUrl = "imageUrl",
        createdBy = "user2Id"
    )

    private val recipe3 = RecipeEntity(
        recipeId = "recipe3Id",
        name = "Recipe 3 Name",
        prepTime = "12 min",
        servings = 3,
        description = "Recipe 3 description",
        isVegetarian = true,
        isVegan = true,
        imageUrl = "imageUrl",
        createdBy = "userId"
    )

    private val recipeIngredient = RecipeIngredientEntity(
        recipeIngredientId = 3463764373,
        ingredientId = "ingredientId",
        recipeId = "recipeId",
        quantity = "4 cans"
    )

    private val recipeIngredient2 = RecipeIngredientEntity(
        recipeIngredientId = 2345274649,
        ingredientId = "ingredient2Id",
        recipeId = "recipeId",
        quantity = "24 dag"
    )

    private val recipeIngredient3 = RecipeIngredientEntity(
        recipeIngredientId = 5936508472,
        ingredientId = "ingredient3Id",
        recipeId = "recipeId",
        quantity = "145 g"
    )

    private val recipeIngredient4 = RecipeIngredientEntity(
        recipeIngredientId = 7497490637,
        ingredientId = "ingredient4Id",
        recipeId = "recipe2Id",
        quantity = "2 kg"
    )

    private val ingredient = IngredientEntity(
        ingredientId = "ingredientId",
        name = "Ingredient Name",
        imageUrl = "imageUrl",
        category = "category"
    )

    private val ingredient2 = IngredientEntity(
        ingredientId = "ingredient2Id",
        name = "Ingredient 2 Name",
        imageUrl = "imageUrl",
        category = "category"
    )

    private val ingredient3 = IngredientEntity(
        ingredientId = "ingredient3Id",
        name = "Ingredient 3 Name",
        imageUrl = "imageUrl",
        category = "category"
    )

    private val ingredient4 = IngredientEntity(
        ingredientId = "ingredient4Id",
        name = "Ingredient 4 Name",
        imageUrl = "imageUrl",
        category = "category"
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = db.recipeDao
        ingredientDao = db.ingredientDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun recipeDao_insertOneRecipe() = runBlocking {
        val recipes = listOf(recipe)
        dao.insertRecipe(recipe)
        val result = dao.getRecipes()

        assertThat(result.size).isEqualTo(1)
        assertThat(result).isEqualTo(recipes)
    }

    @Test
    fun recipeDao_insertOneRecipeWithIngredient() = runBlocking {
        val recipeIngredients = listOf(recipeIngredient)
        dao.insertRecipe(recipe)
        dao.insertRecipeIngredients(recipeIngredients)
        val result = dao.getRecipeWithIngredients("recipeId")

        assertThat(result.recipeIngredients).hasSize(1)
        assertThat(result.recipeIngredients).isEqualTo(recipeIngredients)
    }

    @Test
    fun recipeDao_insertThreeRecipeWithIngredient() {
        runBlocking {
            val recipeIngredients = listOf(recipeIngredient, recipeIngredient2, recipeIngredient3)
            dao.insertRecipe(recipe)
            dao.insertRecipeIngredients(recipeIngredients)
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result.recipeIngredients).hasSize(3)
            assertThat(result.recipeIngredients).containsExactlyElementsIn(recipeIngredients)
        }
    }

    @Test
    fun recipeDao_insertRecipeWithIngredients_oneIngredient() {
        runBlocking {
            val recipeIngredients = listOf(recipeIngredient)
            dao.insertRecipeWithIngredients(recipe,recipeIngredients)
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result.recipe).isEqualTo(recipe)
            assertThat(result.recipeIngredients).hasSize(1)
            assertThat(result.recipeIngredients).containsExactlyElementsIn(recipeIngredients)
        }
    }

    @Test
    fun recipeDao_insertRecipeWithIngredients_threeIngredients() {
        runBlocking {
            val recipeIngredients = listOf(recipeIngredient, recipeIngredient2, recipeIngredient3)
            dao.insertRecipeWithIngredients(recipe,recipeIngredients)
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result.recipe).isEqualTo(recipe)
            assertThat(result.recipeIngredients).hasSize(3)
            assertThat(result.recipeIngredients).containsExactlyElementsIn(recipeIngredients)
        }
    }

    @Test
    fun recipeDao_getRecipes_correctReturnType() {
        runBlocking {
            dao.insertRecipe(recipe)
            val result = dao.getRecipes()

            assertThat(result).hasSize(1)
            assertThat(result).containsExactly(recipe)
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(RecipeEntity::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipes_emptyDb() {
        runBlocking {
            val result = dao.getRecipes()

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipeWithIngredients_correctReturnType() {
        runBlocking {
            val recipeIngredients = listOf(recipeIngredient, recipeIngredient2)
            dao.insertRecipeWithIngredients(recipe, recipeIngredients)
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result.recipe).isEqualTo(recipe)
            assertThat(result.recipeIngredients).hasSize(2)
            assertThat(result.recipeIngredients).containsExactlyElementsIn(recipeIngredients)
            assertThat(result).isInstanceOf(RecipeWithIngredient::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipeWithIngredients_emptyDb() {
        runBlocking {
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result).isNull()
        }
    }

    @Test
    fun recipeDao_getUserRecipes() {
        runBlocking {
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            val result = dao.getUserRecipes("userId")

            assertThat(result).hasSize(2)
            assertThat(result).containsExactlyElementsIn(listOf(recipe, recipe3))
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(RecipeEntity::class.java)
        }
    }

    @Test
    fun recipeDao_getUserRecipes_userHasNoCreatedRecipes() {
        runBlocking {
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            val result = dao.getUserRecipes("user3Id")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun recipeDao_getIngredientsFromRecipe_returnsOnlyIngredientOfTheRecipe() {
        runBlocking {
            dao.insertRecipe(recipe)
            dao.insertRecipeIngredients(listOf(recipeIngredient, recipeIngredient4, recipeIngredient2, recipeIngredient3))
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3, ingredient4))
            val result = dao.getIngredientsFromRecipe("recipeId")

            assertThat(result).hasSize(3)
            assertThat(result).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient3))
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(IngredientEntity::class.java)
        }
    }

    @Test
    fun recipeDao_getIngredientsFromRecipe_recipeHasNoIngredients() {
        runBlocking {
            dao.insertRecipe(recipe3)
            dao.insertRecipeIngredients(listOf(recipeIngredient, recipeIngredient4, recipeIngredient2, recipeIngredient3))
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3, ingredient4))
            val result = dao.getIngredientsFromRecipe("recipe3Id")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun recipeDao_getIngredientsFromRecipe_emptyDb() {
        runBlocking {
            val result = dao.getIngredientsFromRecipe("recipe3Id")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun recipeDao_deleteRecipes() = runBlocking {
        dao.insertRecipe(recipe)
        dao.insertRecipe(recipe2)
        dao.insertRecipe(recipe3)

        val initialState = dao.getRecipes()
        dao.deleteRecipes()
        val result = dao.getRecipes()

        assertThat(initialState).hasSize(3)
        assertThat(result).isEmpty()
    }
}