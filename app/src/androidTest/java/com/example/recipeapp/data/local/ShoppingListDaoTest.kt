package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.ShoppingListEntity
import com.example.recipeapp.data.local.entity.ShoppingListIngredientEntity
import com.example.recipeapp.data.local.relation.ShoppingListWithIngredient
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
class ShoppingListDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    private lateinit var dao: ShoppingListDao
    private lateinit var ingredientDao: IngredientDao

    private val shoppingList = ShoppingListEntity(
        shoppingListId = "shoppingListId",
        name = "Shopping List Name",
        createdBy = "userId"
    )

    private val shoppingList2 = ShoppingListEntity(
        shoppingListId = "shoppingList2Id",
        name = "Shopping List 2 Name",
        createdBy = "userId"
    )

    private val shoppingList3 = ShoppingListEntity(
        shoppingListId = "shoppingList3Id",
        name = "Shopping List 3 Name",
        createdBy = "userId"
    )

    private val shoppingListIngredient = ShoppingListIngredientEntity(
        shoppingListIngredientId = 9836583604,
        ingredientId = "ingredientId",
        shoppingListId = "shoppingListId",
        quantity = "15 g"
    )

    private val shoppingListIngredient2 = ShoppingListIngredientEntity(
        shoppingListIngredientId = 4829563965,
        ingredientId = "ingredient2Id",
        shoppingListId = "shoppingListId",
        quantity = "4 kg"
    )

    private val shoppingListIngredient3 = ShoppingListIngredientEntity(
        shoppingListIngredientId = 8376409365,
        ingredientId = "ingredient3Id",
        shoppingListId = "shoppingList2Id",
        quantity = "3 cans"
    )

    private val shoppingListIngredient4 = ShoppingListIngredientEntity(
        shoppingListIngredientId = 4736585798,
        ingredientId = "ingredient4Id",
        shoppingListId = "shoppingListId",
        quantity = "28 dag"
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
        dao = db.shoppingListDao
        ingredientDao = db.ingredientDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun shoppingListDao_insertOneShoppingList() = runBlocking {
        val shoppingLists = listOf(shoppingList)
        dao.insertShoppingList(shoppingList)
        val result = dao.getShoppingLists()

        assertThat(result).hasSize(1)
        assertThat(result).isEqualTo(shoppingLists)
    }

    @Test
    fun shoppingListDao_insertShoppingListIngredients_oneIngredient() = runBlocking {
        val shoppingListIngredients = listOf(shoppingListIngredient)
        dao.insertShoppingList(shoppingList)
        dao.insertShoppingListIngredients(shoppingListIngredients)
        val result = dao.getShoppingListWithIngredients("shoppingListId")

        assertThat(result.shoppingListIngredients).hasSize(1)
        assertThat(result.shoppingListIngredients).isEqualTo(shoppingListIngredients)
    }

    @Test
    fun shoppingListDao_insertShoppingListIngredients_threeIngredients() {
        runBlocking {
            val shoppingListIngredients = listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient3, shoppingListIngredient4)
            dao.insertShoppingList(shoppingList)
            dao.insertShoppingListIngredients(shoppingListIngredients)
            val result = dao.getShoppingListWithIngredients("shoppingListId")

            assertThat(result.shoppingList).isEqualTo(shoppingList)
            assertThat(result.shoppingListIngredients).hasSize(3)
            assertThat(result.shoppingListIngredients).containsExactlyElementsIn(listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient4))
        }
    }

    @Test
    fun shoppingListDao_insertShoppingListWithIngredients_oneIngredient() = runBlocking {
        val shoppingListIngredients = listOf(shoppingListIngredient)
        dao.insertShoppingListWithIngredients(shoppingList, shoppingListIngredients)
        val result = dao.getShoppingListWithIngredients("shoppingListId")

        assertThat(result.shoppingList).isEqualTo(shoppingList)
        assertThat(result.shoppingListIngredients).hasSize(1)
        assertThat(result.shoppingListIngredients).isEqualTo(shoppingListIngredients)
    }

    @Test
    fun shoppingListDao_insertShoppingListWithIngredients_threeIngredients() {
        runBlocking {
            val shoppingListIngredients = listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient3, shoppingListIngredient4)
            dao.insertShoppingListWithIngredients(shoppingList, shoppingListIngredients)
            val result = dao.getShoppingListWithIngredients("shoppingListId")

            assertThat(result.shoppingList).isEqualTo(shoppingList)
            assertThat(result.shoppingListIngredients).hasSize(3)
            assertThat(result.shoppingListIngredients).containsExactlyElementsIn(listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient4))
        }
    }

    @Test
    fun shoppingListDao_insertShoppingListWithIngredients_noIngredients() = runBlocking {
        dao.insertShoppingListWithIngredients(shoppingList, emptyList())
        val result = dao.getShoppingListWithIngredients("shoppingListId")

        assertThat(result.shoppingList).isEqualTo(shoppingList)
        assertThat(result.shoppingListIngredients).isEmpty()
    }

    @Test
    fun shoppingListDao_getShoppingLists_correctReturnType() = runBlocking {
        dao.insertShoppingList(shoppingList)
        val result = dao.getShoppingLists()

        assertThat(result).hasSize(1)
        assertThat(result).containsExactly(shoppingList)
        assertThat(result).isInstanceOf(List::class.java)
        assertThat(result[0]).isInstanceOf(ShoppingListEntity::class.java)
    }

    @Test
    fun shoppingListDao_getShoppingLists_emptyDb() = runBlocking {
        val result = dao.getShoppingLists()

        assertThat(result).isEmpty()
        assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun shoppingListDao_getShoppingListWithIngredients_correctReturnType() = runBlocking {
        val shoppingListIngredients = listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient3, shoppingListIngredient4)
        dao.insertShoppingListWithIngredients(shoppingList, shoppingListIngredients)
        val result = dao.getShoppingListWithIngredients("shoppingListId")

        assertThat(result.shoppingList).isEqualTo(shoppingList)
        assertThat(result.shoppingListIngredients).hasSize(3)
        assertThat(result.shoppingListIngredients).containsExactlyElementsIn(listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient4))
        assertThat(result).isInstanceOf(ShoppingListWithIngredient::class.java)
    }

    @Test
    fun shoppingListDao_getShoppingListWithIngredients_emptyDb() = runBlocking {
        val result = dao.getShoppingListWithIngredients("shoppingListId")

        assertThat(result).isNull()
    }

    @Test
    fun shoppingListDao_getIngredientsFromShoppingList_returnsOnlyIngredientOfTheShoppingList() {
        runBlocking {
            val shoppingListIngredients = listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient3, shoppingListIngredient4)
            dao.insertShoppingListWithIngredients(shoppingList, shoppingListIngredients)
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3, ingredient4))
            val result = dao.getIngredientsFromShoppingList("shoppingListId")

            assertThat(result).hasSize(3)
            assertThat(result).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient4))
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(IngredientEntity::class.java)
        }
    }

    @Test
    fun shoppingListDao_getIngredientsFromShoppingList_shoppingListHasNoIngredients() {
        runBlocking {
            dao.insertShoppingList(shoppingList)
            dao.insertShoppingListIngredients(listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient3, shoppingListIngredient4))
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3, ingredient4))
            val result = dao.getIngredientsFromShoppingList("shoppingList3Id")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun shoppingListDao_getIngredientsFromShoppingList_emptyDb() {
        runBlocking {
            val result = dao.getIngredientsFromShoppingList("shoppingList3Id")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun shoppingListDao_deleteShoppingList() = runBlocking {
        dao.insertShoppingList(shoppingList)
        dao.insertShoppingList(shoppingList2)
        dao.insertShoppingList(shoppingList3)

        val initialState = dao.getShoppingLists()
        dao.deleteShoppingLists()
        val result = dao.getShoppingLists()

        assertThat(initialState).hasSize(3)
        assertThat(result).isEmpty()
    }

    @Test
    fun shoppingListDao_deleteShoppingListWithIngredients() {
        runBlocking {
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient4))
            dao.insertShoppingListIngredients(listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient4))
            val initialState = dao.getIngredientsFromShoppingList("shoppingListId")

            dao.deleteShoppingListIngredients()
            val result = dao.getIngredientsFromShoppingList("shoppingListId")

            assertThat(initialState).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient4))
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun shoppingListDao_deleteShoppingListsWithIngredients_deleteShoppingListsAndItsIngredients() {
        runBlocking {
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient4))
            dao.insertShoppingListWithIngredients(shoppingList, listOf(shoppingListIngredient, shoppingListIngredient2, shoppingListIngredient4))
            val shoppingListInitialState = dao.getShoppingLists()
            val shoppingListIngredientInitialState = dao.getIngredientsFromShoppingList("shoppingListId")

            dao.deleteShoppingListsWithIngredients()
            val recipeResult = dao.getShoppingLists()
            val recipeIngredientsResult = dao.getIngredientsFromShoppingList("shoppingListId")

            assertThat(shoppingListInitialState).isEqualTo(listOf(shoppingList))
            assertThat(shoppingListIngredientInitialState).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient4))
            assertThat(recipeResult).isEmpty()
            assertThat(recipeIngredientsResult).isEmpty()
        }
    }
}