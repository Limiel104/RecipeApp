package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun CategoryCheckboxItem(
    modifier: Modifier = Modifier,
    category: Category,
    isChecked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange() }
        )

        ImageItem(
            modifier = modifier.size(40.dp),
            contentScale = ContentScale.FillBounds,
            imageUrl = category.imageUrl
        )

        Text(
            text = category.categoryId,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(horizontal = 8.dp)
        )
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
fun CategoryCheckboxItemPreview() {
    RecipeAppTheme {
        Surface {
            CategoryCheckboxItem(
                category = Category("Appetizer", ""),
                isChecked = false,
                onCheckedChange = {}
            )
        }
    }
}