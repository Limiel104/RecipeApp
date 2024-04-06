package com.example.recipeapp.presentation.recipe_details.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun DescriptionTab(
    modifier: Modifier = Modifier
) {
    Text(
        text = "This is a description of a recipe.\nNew line\nAnother line",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, 24.dp)
            .testTag("Description Tab Content")
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
fun DescriptionTabPreview() {
    RecipeAppTheme {
        Surface {
            DescriptionTab()
        }
    }
}