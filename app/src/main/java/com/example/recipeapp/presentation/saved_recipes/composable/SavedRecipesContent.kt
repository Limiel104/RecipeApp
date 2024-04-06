package com.example.recipeapp.presentation.saved_recipes.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.RecipeItem
import com.example.recipeapp.presentation.common.composable.SearchBarItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun SavedRecipesContent(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("Saved Recipes Content")
        ) {
            item {
                SearchBarItem()
            }

            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp,8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recipes",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Newest",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            items(11) {
                RecipeItem(
                    cardHorizontalPadding = 16.dp,
                    cardBottomPadding = 16.dp,
                    onClick = {}
                )
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
fun SavedRecipesContentPreview() {
    RecipeAppTheme {
        SavedRecipesContent()
    }
}