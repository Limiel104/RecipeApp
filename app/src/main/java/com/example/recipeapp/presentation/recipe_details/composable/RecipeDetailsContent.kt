package com.example.recipeapp.presentation.recipe_details.composable

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.presentation.recipe_details.RecipeDetailsState
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    uiState: RecipeDetailsState,
    onTabChanged: (Int) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "Bookmark recipe button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .testTag("Recipe Details Content")
        ) {
            Text(
                text = uiState.recipe.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )

            ImageItem(
                modifier = modifier
                    .fillMaxWidth()
                    .height(180.dp),
                imageUrl = uiState.recipe.imageUrl
            )

            Column() {
                SecondaryTabRow(selectedTabIndex = uiState.secondaryTabState) {
                    uiState.tabTitleList.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.secondaryTabState == index,
                            onClick = { onTabChanged(index) },
                            modifier = Modifier.testTag("$title Tab Title"),
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
            }

            if (uiState.secondaryTabState == 0) {
                IngredientsTab(
                    servings = uiState.recipe.servings,
                    ingredients = uiState.recipe.ingredients
                )
            }
            else {
                DescriptionTab(description = uiState.recipe.description)
            }
        }
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
fun RecipeDetailsContentPreviewIngredientsTab() {
    RecipeAppTheme {
        RecipeDetailsContent(
            uiState = RecipeDetailsState(
                recipe = RecipeWithIngredients(
                    recipeId = "",
                    name = "Recipe name long long long long ",
                    ingredients = getIngredientsWithQuantity(),
                    prepTime = "15 min",
                    servings = 2,
                    description = "This is recipe description",
                    isVegetarian = false,
                    isVegan = false,
                    imageUrl = "",
                    createdBy = "userUID",
                    categories = listOf("Dinner","Chicken"),
                    date = 1234567890
                ),
                secondaryTabState = 0
            ),
            scrollState = rememberScrollState(),
            onTabChanged = {}
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
fun RecipeDetailsContentPreviewDescriptionTab() {
    RecipeAppTheme {
        RecipeDetailsContent(
            uiState = RecipeDetailsState(
                recipe = RecipeWithIngredients(
                    recipeId = "",
                    name = "Recipe name long long long long ",
                    ingredients = getIngredientsWithQuantity(),
                    prepTime = "15 min",
                    servings = 2,
                    description = "This is recipe description",
                    isVegetarian = false,
                    isVegan = false,
                    imageUrl = "",
                    createdBy = "userUID",
                    categories = listOf("Dinner","Chicken"),
                    date = 1234567890
                ),
                secondaryTabState = 1
            ),
            scrollState = rememberScrollState(),
            onTabChanged = {}
        )
    }
}