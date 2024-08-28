package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.presentation.common.composable.AutoComplete
import com.example.recipeapp.presentation.common.composable.RecipeIngredientItem
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientsDialog(
    modifier: Modifier = Modifier,
    ingredient: String,
    isDropDownMenuExpanded: Boolean,
    ingredientsToSelect: List<Ingredient>,
    selectedIngredients: List<Ingredient>,
    onIngredientSuggestionClick: (Ingredient) -> Unit,
    onDropDownMenuExpandedChange: () -> Unit,
    onIngredientChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Select Ingredients") },
                    navigationIcon = {
                        IconButton(onClick = { onDismiss() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear button"
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { onSave() },
                            modifier = modifier.testTag("Save button")
                        ) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("Add ingredients dialog")
            ) {
                AutoComplete(
                    modifier = modifier.padding(horizontal = 8.dp),
                    expanded = isDropDownMenuExpanded,
                    ingredient = ingredient,
                    ingredients = ingredientsToSelect,
                    onExpandedChange = { onDropDownMenuExpandedChange() },
                    onValueChange = { onIngredientChange(it) },
                    onClick = { onIngredientSuggestionClick(it) }
                )

                LazyColumn(
                    modifier = modifier.testTag("Selected ingredients list")
                ) {
                    itemsIndexed(selectedIngredients) { _, selectedIngredient ->
                        RecipeIngredientItem(
                            ingredient = selectedIngredient,
                            quantity = "",
                            dragIndex = "",
                            isReorderModeActivated = false,
                            onClick = {}
                        )
                    }
                }
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
fun CategoriesDialogPreview() {
    RecipeAppTheme {
        AddIngredientsDialog(
            ingredient = "Ingredient Name",
            isDropDownMenuExpanded = true,
            ingredientsToSelect = getIngredientsWithQuantity().keys.toList(),
            selectedIngredients = emptyList(),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onDismiss = {},
            onSave = {}
        )
    }
}