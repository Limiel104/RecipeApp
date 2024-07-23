package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.entity.IngredientEntity
import com.example.recipeapp.data.local.entity.RecipeCategoryEntity
import com.example.recipeapp.data.local.entity.RecipeEntity
import com.example.recipeapp.data.local.entity.RecipeIngredientEntity
import com.example.recipeapp.data.local.relation.RecipeWithCategory
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

    private lateinit var recipe: RecipeEntity
    private lateinit var recipe2: RecipeEntity
    private lateinit var recipe3: RecipeEntity
    private lateinit var recipeCategoryEntity: RecipeCategoryEntity
    private lateinit var recipeCategoryEntity2: RecipeCategoryEntity
    private lateinit var recipeCategoryEntity3: RecipeCategoryEntity
    private lateinit var recipeWithCategory: RecipeWithCategory
    private lateinit var recipeWithCategory2: RecipeWithCategory
    private lateinit var recipeIngredient: RecipeIngredientEntity
    private lateinit var recipeIngredient2: RecipeIngredientEntity
    private lateinit var recipeIngredient3: RecipeIngredientEntity
    private lateinit var recipeIngredient4: RecipeIngredientEntity
    private lateinit var ingredient: IngredientEntity
    private lateinit var ingredient2: IngredientEntity
    private lateinit var ingredient3: IngredientEntity
    private lateinit var ingredient4: IngredientEntity

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: RecipeDatabase
    private lateinit var dao: RecipeDao
    private lateinit var ingredientDao: IngredientDao

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = db.recipeDao
        ingredientDao = db.ingredientDao

        recipe = RecipeEntity(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userId",
            date = 12345623423
        )
        recipe2 = RecipeEntity(
            recipeId = "recipe2Id",
            name = "Recipe 2 Name",
            prepTime = "1 h",
            servings = 2,
            description = "Recipe 2 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "user2Id",
            date = 12345623423
        )

        recipe3 = RecipeEntity(
            recipeId = "recipe3Id",
            name = "Recipe 3 Name",
            prepTime = "12 min",
            servings = 3,
            description = "Recipe 3 description",
            isVegetarian = true,
            isVegan = true,
            imageUrl = "imageUrl",
            createdBy = "userId",
            date = 12345623423
        )

        recipeCategoryEntity = RecipeCategoryEntity(
            recipeId = "recipeId",
            categoryId = 1,
            categoryName = "category1"
        )

        recipeCategoryEntity2 = RecipeCategoryEntity(
            recipeId = "recipeId",
            categoryId = 2,
            categoryName = "category2"
        )

        recipeCategoryEntity3 = RecipeCategoryEntity(
            recipeId = "recipe2Id",
            categoryId = 3,
            categoryName = "category3"
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
                createdBy = "userId",
                date = 12345623423
            ),
            categories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
        )

        recipeWithCategory2 = RecipeWithCategory(
            recipe = RecipeEntity(
                recipeId = "recipe2Id",
                name = "Recipe 2 Name",
                prepTime = "1 h",
                servings = 2,
                description = "Recipe 2 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "user2Id",
                date = 12345623423
            ),
            categories = listOf(recipeCategoryEntity3)
        )

        recipeIngredient = RecipeIngredientEntity(
            recipeIngredientId = 3463764373,
            ingredientId = "ingredientId",
            recipeId = "recipeId",
            quantity = "4 cans"
        )

        recipeIngredient2 = RecipeIngredientEntity(
            recipeIngredientId = 2345274649,
            ingredientId = "ingredient2Id",
            recipeId = "recipeId",
            quantity = "24 dag"
        )

        recipeIngredient3 = RecipeIngredientEntity(
            recipeIngredientId = 5936508472,
            ingredientId = "ingredient3Id",
            recipeId = "recipeId",
            quantity = "145 g"
        )

        recipeIngredient4 = RecipeIngredientEntity(
            recipeIngredientId = 7497490637,
            ingredientId = "ingredient4Id",
            recipeId = "recipe2Id",
            quantity = "2 kg"
        )

        ingredient = IngredientEntity(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient2 = IngredientEntity(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient3 = IngredientEntity(
            ingredientId = "ingredient3Id",
            name = "Ingredient 3 Name",
            imageUrl = "imageUrl",
            category = "category"
        )

        ingredient4 = IngredientEntity(
            ingredientId = "ingredient4Id",
            name = "Ingredient 4 Name",
            imageUrl = "imageUrl",
            category = "category"
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun recipeDao_insertOneRecipe() = runBlocking {
        dao.insertRecipe(recipe)
        val result = dao.getRecipes("")

        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].recipe).isEqualTo(recipe)
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
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipeWithIngredients(recipe,recipeIngredients, recipeCategories)
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
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipeWithIngredients(recipe,recipeIngredients, recipeCategories)
            val result = dao.getRecipeWithIngredients("recipeId")

            assertThat(result.recipe).isEqualTo(recipe)
            assertThat(result.recipeIngredients).hasSize(3)
            assertThat(result.recipeIngredients).containsExactlyElementsIn(recipeIngredients)
        }
    }

    @Test
    fun recipeDao_insertRecipeCategory_returnsRecipeWithItsCategories() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipe(recipe)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipes("")

            assertThat(result).hasSize(1)
            assertThat(result[0]).isEqualTo(recipeWithCategory)
            assertThat(result[0].recipe).isEqualTo(recipe)
            assertThat(result[0].categories).containsExactlyElementsIn(recipeCategories)
        }
    }

    @Test
    fun recipeDao_getRecipes_correctReturnType() {
        runBlocking {
            dao.insertRecipeWithIngredients(recipe, emptyList(), listOf(recipeCategoryEntity, recipeCategoryEntity2))
            val result = dao.getRecipes("")

            assertThat(result).hasSize(1)
            assertThat(result).containsExactly(recipeWithCategory)
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(RecipeWithCategory::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipes_emptyDb() {
        runBlocking {
            val result = dao.getRecipes("")

            assertThat(result).isEmpty()
            assertThat(result).isInstanceOf(List::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipes_returnOnlyRecipesMatchingQuery() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipes("3")

            assertThat(result).hasSize(1)
            assertThat(result[0].recipe).isEqualTo(recipe3)
            assertThat(result[0].categories).isEmpty()
        }
    }

    @Test
    fun recipeDao_getRecipes_noRecipesMatchQuery() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipes("query")

            assertThat(result).isEmpty()
        }
    }

    @Test
    fun recipeDao_getRecipeWithIngredients_correctReturnType() {
        runBlocking {
            val recipeIngredients = listOf(recipeIngredient, recipeIngredient2)
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipeWithIngredients(recipe, recipeIngredients, recipeCategories)
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
    fun recipeDao_getRecipesFromCategory_correctReturnType() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipe(recipe)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipesFromCategory("","category1")

            assertThat(result[0].recipe).isEqualTo(recipe)
            assertThat(result[0].categories).hasSize(2)
            assertThat(result[0].categories).containsExactlyElementsIn(recipeCategories)
            assertThat(result[0]).isInstanceOf(RecipeWithCategory::class.java)
        }
    }

    @Test
    fun recipeDao_getRecipesFromCategory_emptyDb() {
        runBlocking {
            val result = dao.getRecipesFromCategory("","category1")

            assertThat(result).isEmpty()
        }
    }

    @Test
    fun recipeDao_getRecipesFromCategory_returnOnlyRecipesFormTheCategory() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipesFromCategory("","category2")

            assertThat(result).hasSize(1)
            assertThat(result[0].recipe).isEqualTo(recipe)
            assertThat(result[0].categories).containsExactlyElementsIn(listOf(recipeCategoryEntity, recipeCategoryEntity2))
        }
    }

    @Test
    fun recipeDao_getRecipesFromCategory_returnOnlyRecipesMatchingQuery() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipesFromCategory("2","category3")

            assertThat(result).hasSize(1)
            assertThat(result[0].recipe).isEqualTo(recipe2)
            assertThat(result[0].categories).containsExactlyElementsIn(listOf(recipeCategoryEntity3))
        }
    }

    @Test
    fun recipeDao_getRecipesFromCategory_noRecipesMatchQuery() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipe(recipe2)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getRecipesFromCategory("query","category3")

            assertThat(result).isEmpty()
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
    fun recipeDao_getCategoriesFromRecipe() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getCategoriesFromRecipe("recipeId")

            assertThat(result).hasSize(2)
            assertThat(result).containsExactlyElementsIn(listOf("category1","category2"))
            assertThat(result).isInstanceOf(List::class.java)
            assertThat(result[0]).isInstanceOf(String::class.java)}
    }

    @Test
    fun recipeDao_getCategoriesFromRecipe_recipeDoesNotHaveCategories() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2, recipeCategoryEntity3)
            dao.insertRecipe(recipe3)
            dao.insertRecipeCategories(recipeCategories)
            val result = dao.getCategoriesFromRecipe("recipe3Id")

            assertThat(result).isEmpty()
        }
    }

    @Test
    fun recipeDao_deleteRecipes() = runBlocking {
        dao.insertRecipe(recipe)
        dao.insertRecipe(recipe2)
        dao.insertRecipe(recipe3)

        val initialState = dao.getRecipes("")
        dao.deleteRecipes()
        val result = dao.getRecipes("")

        assertThat(initialState).hasSize(3)
        assertThat(result).isEmpty()
    }

    @Test
    fun recipeDao_deleteRecipeWithIngredients() {
        runBlocking {
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3))
            dao.insertRecipeIngredients(listOf(recipeIngredient, recipeIngredient2, recipeIngredient3))
            val initialState = dao.getIngredientsFromRecipe("recipeId")

            dao.deleteAllRecipes()
            val result = dao.getIngredientsFromRecipe("recipeId")

            assertThat(initialState).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient3))
            assertThat(result).isEmpty()
        }
    }

    @Test
    fun recipeDao_deleteRecipeCategories() {
        runBlocking {
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipeCategories(recipeCategories)
            val categoriesInitialState = dao.getCategoriesFromRecipe("recipeId")

            dao.deleteRecipesCategories()
            val categoriesResult = dao.getCategoriesFromRecipe("recipeId")

            assertThat(categoriesInitialState).hasSize(2)
            assertThat(categoriesInitialState).containsExactlyElementsIn(listOf("category1","category2"))
            assertThat(categoriesResult).isEmpty()
        }
    }

    @Test
    fun recipeDao_deleteAllRecipes_deleteAllRecipesAndItsIngredientsAndCategories() {
        runBlocking {
            ingredientDao.insertIngredients(listOf(ingredient, ingredient2, ingredient3))
            val recipeIngredients = listOf(recipeIngredient, recipeIngredient2, recipeIngredient3)
            val recipeCategories = listOf(recipeCategoryEntity, recipeCategoryEntity2)
            dao.insertRecipeWithIngredients(recipe, recipeIngredients, recipeCategories)
            val recipeInitialState = dao.getRecipes("")
            val recipeIngredientInitialState = dao.getIngredientsFromRecipe("recipeId")

            dao.deleteAllRecipes()
            val recipeResult = dao.getRecipes("")
            val recipeIngredientsResult = dao.getIngredientsFromRecipe("recipeId")

            assertThat(recipeInitialState).isEqualTo(listOf(recipeWithCategory))
            assertThat(recipeIngredientInitialState).containsExactlyElementsIn(listOf(ingredient, ingredient2, ingredient3))
            assertThat(recipeResult).isEmpty()
            assertThat(recipeIngredientsResult).isEmpty()
        }
    }
}