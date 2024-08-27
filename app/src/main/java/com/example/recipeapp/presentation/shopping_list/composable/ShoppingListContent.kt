package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.presentation.shopping_list.ShoppingListState
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListContent(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: ShoppingListState,
    onIngredientSuggestionClick: (Ingredient) -> Unit,
    onDropDownMenuExpandedChange: () -> Unit,
    onIngredientChange: (String) -> Unit,
    onAddIngredientsDialogDismiss: () -> Unit,
    onAddIngredientsSave: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.shopping_list)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu button"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add button"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .testTag("Shopping List Content")
        ) {
            item {
                Text(
                    text = "123 items",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    modifier = modifier
                        .padding(bottom = 8.dp)
                        .padding(start = 16.dp)
                )
            }

            items(5) {
                ShoppingListCategoryItem()
            }
        }

        if(uiState.isAddIngredientsDialogOpened) {
            AddIngredientsDialog(
                ingredient = uiState.ingredient,
                isDropDownMenuExpanded = uiState.isDropDownMenuExpanded,
                ingredientsToSelect = uiState.ingredientsToSelect,
                selectedIngredients = uiState.selectedIngredients.keys.toList(),
                onIngredientSuggestionClick = { onIngredientSuggestionClick(it) },
                onDropDownMenuExpandedChange = { onDropDownMenuExpandedChange() },
                onIngredientChange = { onIngredientChange(it) },
                onDismiss = { onAddIngredientsDialogDismiss() },
                onSave = { onAddIngredientsSave() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ShoppingListContentPreview() {
    RecipeAppTheme {
        ShoppingListContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            uiState = ShoppingListState(),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onAddIngredientsDialogDismiss = {},
            onAddIngredientsSave = {},
            onAddButtonClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ShoppingListContentPreviewDialog() {
    RecipeAppTheme {
        ShoppingListContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            uiState = ShoppingListState(isAddIngredientsDialogOpened = true),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onAddIngredientsDialogDismiss = {},
            onAddIngredientsSave = {},
            onAddButtonClick = {}
        )
    }
}