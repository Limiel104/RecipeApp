package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.IngredientEntity
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
class IngredientDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    private lateinit var dao: IngredientDao

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
        dao = db.ingredientDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun ingredientDao_insertOneIngredient() = runBlocking {
        val ingredients = listOf(ingredient)
        dao.insertIngredients(ingredients)
        val result = dao.getIngredients()

        assertThat(result).hasSize(1)
        assertThat(result).isEqualTo(ingredients)
    }

    @Test
    fun ingredientDao_insertFourIngredients() = runBlocking {
        val ingredients = listOf(ingredient, ingredient2, ingredient3, ingredient4)
        dao.insertIngredients(ingredients)
        val result = dao.getIngredients()

        assertThat(result).hasSize(4)
        assertThat(result).isEqualTo(ingredients)
    }

    @Test
    fun ingredientDao_getIngredient_getIngredientById() = runBlocking {
        val ingredients = listOf(ingredient, ingredient2, ingredient3, ingredient4)
        dao.insertIngredients(ingredients)
        val result = dao.getIngredient("ingredient2Id")

        assertThat(result).isEqualTo(ingredient2)
        assertThat(result).isInstanceOf(IngredientEntity::class.java)
    }

    @Test
    fun ingredientDao_getIngredient_getIngredientById_ingredientNotInDb() = runBlocking {
        val ingredients = listOf(ingredient, ingredient2, ingredient3, ingredient4)
        dao.insertIngredients(ingredients)
        val result = dao.getIngredient("ingredient5Id")

        assertThat(result).isNull()
    }

    @Test
    fun ingredientDao_getIngredients_correctReturnType() = runBlocking {
        val ingredients = listOf(ingredient, ingredient2, ingredient3, ingredient4)
        dao.insertIngredients(ingredients)
        val result = dao.getIngredients()

        assertThat(result).hasSize(4)
        assertThat(result).isInstanceOf(List::class.java)
        assertThat(result[0]).isInstanceOf(IngredientEntity::class.java)
    }

    @Test
    fun ingredientDao_getIngredients_emptyDb() = runBlocking {
        val result = dao.getIngredients()

        assertThat(result).isEmpty()
        assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun ingredientDao_deleteIngredients() = runBlocking {
        val ingredients = listOf(ingredient, ingredient2, ingredient3, ingredient4)
        dao.insertIngredients(ingredients)

        val initialState = dao.getIngredients()
        dao.deleteIngredients()
        val result = dao.getIngredients()

        assertThat(initialState).hasSize(4)
        assertThat(result).isEmpty()
    }
}