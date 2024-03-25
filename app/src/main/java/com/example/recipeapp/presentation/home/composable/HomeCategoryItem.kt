package com.example.recipeapp.presentation.home.composable

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun HomeCategoryItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.size(70.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            ImageItem(
                modifier = modifier
                    .fillMaxSize()
            )

            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier
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
        HomeCategoryItem()
    }
}