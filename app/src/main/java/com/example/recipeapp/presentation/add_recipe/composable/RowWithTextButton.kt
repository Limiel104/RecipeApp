package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RowWithTextButton(
    modifier: Modifier = Modifier,
    sectionName: String,
    buttonText: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(sectionName),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sectionName,
            style = MaterialTheme.typography.titleMedium
        )

        TextButton(onClick = {}) {
            Text(text = buttonText)
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
fun RowWithTextButtonPreview() {
    RecipeAppTheme {
        RowWithTextButton(
            sectionName = "Servings",
            buttonText = "Set servings"
        )
    }
}