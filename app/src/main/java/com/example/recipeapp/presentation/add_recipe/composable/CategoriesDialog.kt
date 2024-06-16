package com.example.recipeapp.presentation.add_recipe.composable

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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDialog(
    modifier: Modifier = Modifier,
    categories: Map<Category, Boolean>,
    onSave: () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text(text = "Select recipe categories") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
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
            ) {
                LazyColumn() {
                    itemsIndexed(categories.keys.toList()) { _, category ->
                        categories[category]?.let {
                            CategoryCheckboxItem(
                                category = category,
                                isChecked = it,
                                onCheckedChange = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getCategories(): Map<Category, Boolean> {
    return mapOf(
        Pair(Category("Appetizer",""), false),
        Pair(Category("Chicken",""), true),
        Pair(Category("Dinner",""), true),
        Pair(Category("Soup",""), false),
        Pair(Category("Stew",""), true)
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
fun CategoriesDialogPreview() {
    RecipeAppTheme {
        CategoriesDialog(
            categories = getCategories(),
            onSave = {}
        )
    }
}