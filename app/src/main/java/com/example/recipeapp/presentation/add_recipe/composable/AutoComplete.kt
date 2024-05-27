package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
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
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        val filterOpts = ingredients.filter {
            it.name.contains(ingredient, ignoreCase = true)
        }

        Log.i("TAG567",filterOpts.toString())

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            filterOpts.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.name) },
                    onClick = { onClick() },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }

//        val filterOpts = ingredients.filter {
//            it.name.contains(ingredient, ignoreCase = true)
//        }
//
//        Log.i("TAG567",filterOpts.toString())
//
//        if (filterOpts.isNotEmpty()) {
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { onDismissRequest() }
//            ) {
//                filterOpts.forEach { ingredient ->
//                    DropdownMenuItem(
//                        text = { Text(text = ingredient.name) },
//                        onClick = { onClick() }
//                    )
//                }
//            }
//        }
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
                onDismissRequest = {},
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
                onDismissRequest = {},
                onClick = {}
            )
        }
    }
}