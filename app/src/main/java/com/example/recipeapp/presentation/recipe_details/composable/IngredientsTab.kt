package com.example.recipeapp.presentation.recipe_details.composable

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.RecipeIngredientItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun IngredientsTab(
    modifier: Modifier = Modifier
) {
    Column() {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("Ingredients Tab Content"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.HorizontalRule,
                    contentDescription = "Less button",
                    modifier = modifier.border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
                )
            }

            Text(
                text = "1234 Servings",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = modifier
                    .padding(start = 16.dp)
                    .padding(end = 16.dp)
            )

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "More button",
                    modifier = modifier.border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
                )
            }
        }

        Column(
            modifier = modifier
                .padding(bottom = 20.dp)
                .padding(horizontal = 16.dp)
        ) {
            for (i in 1..7) {
                RecipeIngredientItem()

                if (i != 7) {
                    HorizontalDivider()
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
fun IngredientsTabPreview() {
    RecipeAppTheme {
        Surface {
            IngredientsTab()
        }
    }
}