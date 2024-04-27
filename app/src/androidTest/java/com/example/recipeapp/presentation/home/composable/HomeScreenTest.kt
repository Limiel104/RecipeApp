package com.example.recipeapp.presentation.home.composable

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.ui.theme.RecipeAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class HomeScreenTest {

    private lateinit var category: Category
    private lateinit var category2: Category
    private lateinit var category3: Category
    private lateinit var category4: Category
    private lateinit var category5: Category
    private lateinit var category6: Category
    private lateinit var recipe: Recipe
    private lateinit var recipe2: Recipe
    private lateinit var recipe3: Recipe
    private lateinit var recipe4: Recipe
    private lateinit var recipe5: Recipe
    private lateinit var recipe6: Recipe
    private lateinit var recipe7: Recipe

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        category = Category(
            categoryId = "Appetizer",
            imageUrl = ""
        )

        category2 = Category(
            categoryId = "Beef",
            imageUrl = ""
        )

        category3 = Category(
            categoryId = "Dinner",
            imageUrl = ""
        )

        category4 = Category(
            categoryId = "Fish",
            imageUrl = ""
        )

        category5 = Category(
            categoryId = "Pork",
            imageUrl = ""
        )

        category6 = Category(
            categoryId = "Stew",
            imageUrl = ""
        )

        recipe = Recipe(
            recipeId = "recipeId",
            name = "Recipe Name",
            prepTime = "4 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Dinner")
        )

        recipe2 = Recipe(
            recipeId = "recipe2Id",
            name = "Recipe2 Name",
            prepTime = "55 min",
            servings = 6,
            description = "Recipe2 description",
            isVegetarian = true,
            isVegan = true,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Pizza","Dinner")
        )

        recipe3 = Recipe(
            recipeId = "recipe3Id",
            name = "Recipe3 Name",
            prepTime = "4,5 h",
            servings = 1,
            description = "Recipe3 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "user3Id",
            categories = listOf("Dinner")
        )

        recipe4 = Recipe(
            recipeId = "recipe4Id",
            name = "Recipe4 Name",
            prepTime = "1h",
            servings = 4,
            description = "Recipe4 description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "",
            createdBy = "user2Id",
            categories = listOf("Stew")
        )

        recipe5 = Recipe(
            recipeId = "recipe5Id",
            name = "Recipe5 Name",
            prepTime = "45 min",
            servings = 6,
            description = "Recipe5 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Appetizer","Fish")
        )

        recipe6 = Recipe(
            recipeId = "recipe6Id",
            name = "Recipe6 Name",
            prepTime = "30 min",
            servings = 3,
            description = "Recipe6 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userId",
            categories = listOf("Dinner","Pork","Stew")
        )

        recipe7 = Recipe(
            recipeId = "recipe7Id",
            name = "Recipe7 Name",
            prepTime = "13 min",
            servings = 5,
            description = "Recipe7 description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "user2Id",
            categories = listOf("Appetizer")
        )
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { HomeScreen(navController = navController) }
        }
    }

    private fun setScreenState(
        recipes: List<Recipe> = listOf(recipe, recipe2, recipe3, recipe4, recipe5, recipe6, recipe7),
        query: String = "",
        searchSuggestions: List<SearchSuggestion> = emptyList(),
        categories: List<Category> = listOf(category, category2, category3, category4, category5, category6),
        selectedCategory: String = "",
        isSearchActive: Boolean = false,
        isLoading: Boolean = false
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme() {
                HomeContent(
                    recipes = recipes,
                    query = query,
                    searchSuggestions = searchSuggestions,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    isSearchActive = isSearchActive,
                    isLoading = isLoading,
                    onRecipeSelected = {},
                    onQueryChange = {},
                    onActiveChange = {},
                    onSearchClicked = {},
                    onClearClicked = {},
                    onSearchSuggestionClicked = {},
                    onSelectedCategory = {}
                )
            }
        }
    }

    @Test
    fun searchBar_homeScreen_isClickable() {
        setScreen()
        searchBarIsDisplayed()
        composeRule.onNodeWithTag("Search Bar").performClick()
    }

    @Test
    fun topCategoriesSection_homeScreen_swipesLeftAndRight() {
        setScreenState()
        val firstCategoryName = "Appetizer category"
        val lastCategoryName = "Stew category"

        composeRule
            .onNodeWithTag("Categories Section")
            .assertIsDisplayed()

        composeRule.onNodeWithTag(firstCategoryName).assertIsDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsNotDisplayed()

        composeRule.onNodeWithTag("Categories Section").performTouchInput { swipeLeft() }

        composeRule.onNodeWithTag(firstCategoryName).assertIsNotDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsDisplayed()

        composeRule.onNodeWithTag("Categories Section").performTouchInput { swipeRight() }

        composeRule.onNodeWithTag(firstCategoryName).assertIsDisplayed()
        composeRule.onNodeWithTag(lastCategoryName).assertIsNotDisplayed()
    }

    @Test
    fun homeScreen_allSectionsSwipeVertically() {
        setScreenState()

        searchBarIsDisplayed()
        categoriesSectionIsDisplayed()
        recipesTextIsDisplayed()

        composeRule
            .onNodeWithTag("Home Content")
            .performTouchInput { swipeUp() }

        searchBarIsDisplayed()
        categoriesSectionIsNotDisplayed()
        recipesTextIsNotDisplayed()

        composeRule
            .onNodeWithTag("Home Lazy Column")
            .performTouchInput { swipeDown() }

        searchBarIsDisplayed()
        categoriesSectionIsDisplayed()
        recipesTextIsDisplayed()
    }

    private fun searchBarIsDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsDisplayed()

    private fun searchBarIsNotDisplayed() = composeRule
        .onNodeWithTag("Search Bar")
        .assertIsNotDisplayed()

    private fun categoriesSectionIsDisplayed() = composeRule
        .onNodeWithTag("Categories Section")
        .assertIsDisplayed()

    private fun categoriesSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Categories Section")
        .assertIsNotDisplayed()

    private fun recipesTextIsDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsDisplayed()

    private fun recipesTextIsNotDisplayed() = composeRule
        .onNode(hasText("Recipes"))
        .assertIsNotDisplayed()
}