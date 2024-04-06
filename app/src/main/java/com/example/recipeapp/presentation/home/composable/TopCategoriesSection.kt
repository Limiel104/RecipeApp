package com.example.recipeapp.presentation.home.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun TopCategoriesSection(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .testTag("Top Categories Section")
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 16.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = "Top Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .padding(start = 16.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tempCategoryArray = listOf("Category 1","Category 2","Category 3","Category 4","Category 5","Category 6","Category 7","Category 8")

                itemsIndexed(tempCategoryArray) { index, item ->
                    when (index) {
                        0 -> {
                            HomeCategoryItem(
                                modifier = Modifier.padding(start = 16.dp),
                                categoryName = tempCategoryArray[index]
                            )
                        }
                        tempCategoryArray.lastIndex -> {
                            HomeCategoryItem(
                                modifier = Modifier.padding(end = 16.dp),
                                categoryName = tempCategoryArray[index]
                            )
                        }
                        else -> {
                            HomeCategoryItem(
                                categoryName = tempCategoryArray[index]
                            )
                        }
                    }
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
fun TopCategoriesSectionPreview() {
    RecipeAppTheme {
        TopCategoriesSection()
    }
}