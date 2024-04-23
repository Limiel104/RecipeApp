package com.example.recipeapp.presentation.home.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.presentation.common.composable.RecipeItem
import com.example.recipeapp.presentation.common.composable.SearchBarItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    recipes: List<Recipe>,
    query: String,
    searchSuggestions: List<SearchSuggestion>,
    categories: List<Category>,
    selectedCategory: String,
    isSearchActive: Boolean,
    isLoading: Boolean,
    onRecipeSelected: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onActiveChange: () -> Unit,
    onSearchClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onSearchSuggestionClicked: (String) -> Unit,
    onSelectedCategory: (String) -> Unit
    ) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .testTag("Home Content")
        ) {
            SearchBarItem(
                query = query,
                searchSuggestions = searchSuggestions,
                isSearchActive = isSearchActive,
                onQueryChange = { onQueryChange(it) },
                onActiveChange = { onActiveChange() },
                onSearchClicked = { onSearchClicked() },
                onClear = { onClearClicked() },
                onSearchSuggestionClicked = { onSearchSuggestionClicked(it) }
            )

            if(!isSearchActive) {
                Spacer(modifier = modifier.padding(bottom = 12.dp))
            }

            LazyColumn {
                item {
                    CategoriesSection(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onClick = { onSelectedCategory(it) }
                    )
                }

                item {
                    Text(
                        text = "Recipes",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier
                            .padding(16.dp,8.dp)
                    )
                }

                itemsIndexed(recipes) { _, recipe ->
                    RecipeItem(
                        recipe = recipe,
                        cardHorizontalPadding = 16.dp,
                        cardBottomPadding = 16.dp,
                        onClick = { onRecipeSelected(recipe.recipeId) }
                    )
                }
            }
        }
    }
}

private fun getRecipes(): List<Recipe> {
    val recipe = Recipe(
        recipeId = "recipeId",
        name = "Recipe Name",
        prepTime = "40 min",
        servings = 4,
        description = "Recipe description",
        isVegetarian = true,
        isVegan = false,
        imageUrl = "imageUrl",
        createdBy = "userId",
        categories = emptyList()
    )

    return listOf(recipe, recipe, recipe, recipe, recipe, recipe)
}

private fun getCategories(): List<Category> {
    return listOf(
        Category("Appetizer",""),
        Category("Chicken",""),
        Category("Dinner",""),
        Category("Soup",""),
        Category("Stew","")
    )
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeContentPreview() {
    RecipeAppTheme {
       HomeContent(
            recipes = getRecipes(),
            query = "",
            searchSuggestions = emptyList(),
            categories = getCategories(),
            selectedCategory = "",
            isSearchActive = false,
            isLoading = false,
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

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeContentPreviewSearchIsActive() {
    RecipeAppTheme {
        HomeContent(
            recipes = getRecipes(),
            query = "Search query",
            searchSuggestions = emptyList(),
            categories = getCategories(),
            selectedCategory = "",
            isSearchActive = true,
            isLoading = false,
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