package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    ingredient: String,
    ingredients: List<Ingredient>,
    onExpandedChange: () -> Unit,
    onValueChange: (String) -> Unit,
    onClick: (Ingredient) -> Unit
) {
    val filterOpts = ingredients.filter {
        it.name.contains(ingredient, ignoreCase = true)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() },
        modifier = modifier
            .padding(bottom = 24.dp)
            .testTag("Add recipe type ingredient name EDDM")
    ) {
        OutlinedTextField(
            value = ingredient,
            onValueChange = { onValueChange(it) },
            label = { Text(text = stringResource(id = R.string.type_ingr_name)) },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .testTag("Autocomplete TF")
        )

        if(filterOpts.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange() },
                modifier = modifier
                    .exposedDropdownSize(true)
                    .testTag("Autocomplete DDM"),
                properties = PopupProperties(focusable = false)
            ) {
                filterOpts.forEach { ingredientSuggestion ->
                    DropdownMenuItem(
                        text = { Text(text = ingredientSuggestion.name) },
                        onClick = { onClick(ingredientSuggestion) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

private fun getIngredients(): List<Ingredient> {
    return listOf(
        Ingredient(
            ingredientId = "ingredientId",
            name = "Ingredient Name",
            imageUrl = "imageUrl",
            category = "category"
        ),
        Ingredient(
            ingredientId = "ingredient2Id",
            name = "Ingredient 2 Name",
            imageUrl = "image2Url",
            category = "category"
        ),
        Ingredient(
            ingredientId = "ingredient3Id",
            name = "Ingredient 3 Name",
            imageUrl = "image3Url",
            category = "category2"
        )
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
fun AutoCompletePreview() {
    RecipeAppTheme {
        Surface {
            AutoComplete(
                expanded = false,
                ingredient = "",
                ingredients = getIngredients(),
                onExpandedChange = {},
                onValueChange = {},
                onClick = {}
            )
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
fun AutoCompletePreviewExpanded() {
    RecipeAppTheme {
        Surface {
            AutoComplete(
                expanded = true,
                ingredient = "ingre",
                ingredients = getIngredients(),
                onExpandedChange = {},
                onValueChange = {},
                onClick = {}
            )
        }
    }
}