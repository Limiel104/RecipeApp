package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.SavedRecipeEntity
import com.example.recipeapp.data.local.relation.RecipeWithCategory
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
class SavedRecipeDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    private lateinit var dao: SavedRecipeDao
    private lateinit var recipeDao: RecipeDao

    private val savedRecipe = SavedRecipeEntity(
        savedRecipeId = "savedRecipeId",
        recipeId = "recipeId",
        userId = "userId"
    )

    private val savedRecipe2 = SavedRecipeEntity(
        savedRecipeId = "savedRecipe2Id",
        recipeId = "recipe2Id",
        userId = "userId"
    )

    private val savedRecipe3 = SavedRecipeEntity(
        savedRecipeId = "savedRecipe3Id",
        recipeId = "recipe3Id",
        userId = "userId"
    )

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

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = db.savedRecipeDao
        recipeDao = db.recipeDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun savedRecipeDao_insertOneIngredient() = runBlocking {
        val savedRecipes = listOf(savedRecipe)
        dao.insertSavedRecipes(savedRecipes)
        recipeDao.insertRecipe(recipe)
        recipeDao.insertRecipe(recipe2)
        recipeDao.insertRecipe(recipe3)
        val result = dao.getSavedRecipes()

        assertThat(result).hasSize(1)
    }

    @Test
    fun savedRecipeDao_insertFourIngredients() = runBlocking {
        val savedRecipes = listOf(savedRecipe, savedRecipe2, savedRecipe3)
        dao.insertSavedRecipes(savedRecipes)
        recipeDao.insertRecipe(recipe)
        recipeDao.insertRecipe(recipe2)
        recipeDao.insertRecipe(recipe3)
        val result = dao.getSavedRecipes()

        assertThat(result).hasSize(3)
    }

    @Test
    fun savedRecipeDao_getSavedRecipes_correctReturnType() = runBlocking {
        val savedRecipes = listOf(savedRecipe, savedRecipe2, savedRecipe3)
        dao.insertSavedRecipes(savedRecipes)
        recipeDao.insertRecipe(recipe)
        recipeDao.insertRecipe(recipe2)
        recipeDao.insertRecipe(recipe3)
        val result = dao.getSavedRecipes()

        assertThat(result).hasSize(3)
        assertThat(result).isInstanceOf(List::class.java)
        assertThat(result[0]).isInstanceOf(RecipeWithCategory::class.java)
    }

    @Test
    fun savedRecipeDao_getSavedRecipes_noRecipesInDb() = runBlocking {
        val savedRecipes = listOf(savedRecipe, savedRecipe2, savedRecipe3)
        dao.insertSavedRecipes(savedRecipes)
        val result = dao.getSavedRecipes()

        assertThat(result).isEmpty()
        assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun savedRecipeDao_getSavedRecipes_emptyDb() = runBlocking {
        val result = dao.getSavedRecipes()

        assertThat(result).isEmpty()
        assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun savedRecipeDao_deleteRecipes() = runBlocking {
        val savedRecipes = listOf(savedRecipe, savedRecipe2, savedRecipe3)
        dao.insertSavedRecipes(savedRecipes)
        recipeDao.insertRecipe(recipe)
        recipeDao.insertRecipe(recipe2)
        recipeDao.insertRecipe(recipe3)

        val initialState = dao.getSavedRecipes()
        dao.deleteSavedRecipes()
        val result = dao.getSavedRecipes()

        assertThat(initialState).hasSize(3)
        assertThat(result).isEmpty()
    }
}