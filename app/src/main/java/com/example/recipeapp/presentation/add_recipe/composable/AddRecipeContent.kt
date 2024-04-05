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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.RecipeIngredientItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Add recipe") },
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
                text = "Title",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            AddPhotoCard()

            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Reorder",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = "Tap to edit, swipe to delete",
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
                value = "",
                label = { Text(text = "Type ingredient name") },
                onValueChange = {},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            RowWithTextButton(
                sectionName = "Servings",
                buttonText = "Set servings"
            )

            RowWithTextButton(
                sectionName = "Prep time",
                buttonText = "Set time"
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
            scrollState = rememberScrollState()
        )
    }
}