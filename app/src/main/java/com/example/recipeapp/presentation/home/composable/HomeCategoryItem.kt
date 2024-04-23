package com.example.recipeapp.presentation.home.composable

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun HomeCategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .size(90.dp)
            .clickable { onClick(category.categoryId) }
            .testTag("TCS ${category.categoryId}"),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageItem(
                modifier = Modifier.fillMaxSize(),
                imageUrl = category.imageUrl,
                contentScale = ContentScale.Crop,
                isGrayScale = isSelected
            )

            Text(
                text = category.categoryId,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
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
fun HomeCategoryItemPreview() {
    RecipeAppTheme {
        HomeCategoryItem(
            category = Category("Appetizer",""),
            isSelected = false,
            onClick = {}
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
fun HomeCategoryItemPreviewStartPadding() {
    RecipeAppTheme {
        HomeCategoryItem(
            modifier = Modifier.padding(start = 16.dp),
            category = Category("Appetizer",""),
            isSelected = false,
            onClick = {}
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
fun HomeCategoryItemPreviewEndPadding() {
    RecipeAppTheme {
        HomeCategoryItem(
            modifier = Modifier.padding(start = 16.dp),
            category = Category("Appetizer",""),
            isSelected = false,
            onClick = {}
        )
    }
}