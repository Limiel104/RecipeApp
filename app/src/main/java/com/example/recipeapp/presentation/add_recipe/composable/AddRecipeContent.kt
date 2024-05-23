package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.presentation.common.composable.RecipeIngredientItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    title: String,
    titleError: String?,
    description: String,
    descriptionError: String?,
    ingredient: String,
    ingredientError: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIngredientChange: (String) -> Unit,
    onAddRecipe: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_recipe)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAddRecipe() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add recipe button"
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
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .testTag("Add Recipe Content")
        ) {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text(text = stringResource(id = R.string.title)) },
                placeholder = { Text(text = stringResource(id = R.string.title)) },
                supportingText = {
                    if (titleError != null) {
                        Text(text = titleError)
                    } },
                isError = titleError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("Add recipe title TF")
            )

            AddPhotoCard()

            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { onDescriptionChange(it) },
                label = { Text(text = stringResource(id = R.string.description)) },
                placeholder = { Text(text = stringResource(id = R.string.description)) },
                supportingText = {
                    if (descriptionError != null) {
                        Text(text = descriptionError)
                    } },
                isError = descriptionError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("Add recipe description TF")
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.ingredients),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(id = R.string.reorder),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = stringResource(id = R.string.tap_or_swipe),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Light,
                modifier = modifier.padding(bottom = 20.dp)
            )

            Column(
                modifier = modifier
                    .padding(bottom = 20.dp)
            ) {
                for (i in 1..4) {
                    RecipeIngredientItem()

                    if (i != 4) {
                        HorizontalDivider()
                    }
                }
            }

            OutlinedTextField(
                value = ingredient,
                onValueChange = { onIngredientChange(it) },
                label = { Text(text = stringResource(id = R.string.type_ingr_name)) },
                placeholder = { Text(text = stringResource(id = R.string.email)) },
                supportingText = {
                    if (ingredientError != null) {
                        Text(text = ingredientError)
                    } },
                isError = ingredientError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("Add recipe type ingredient name TF")
            )

            RowWithTextButton(
                sectionName = stringResource(id = R.string.servings),
                buttonText = stringResource(id = R.string.set_servings)
            )

            RowWithTextButton(
                sectionName = stringResource(id = R.string.prep_time),
                buttonText = stringResource(id = R.string.set_time)
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
fun AddRecipeContentPreview() {
    RecipeAppTheme {
        AddRecipeContent(
            scrollState = rememberScrollState(),
            title = "",
            titleError = "",
            description = "",
            ingredient = "",
            ingredientError = "",
            descriptionError = "",
            onIngredientChange = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onAddRecipe = {}
        )
    }
}