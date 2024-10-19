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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun CategoriesSection(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategory: String,
    onClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .testTag("Categories Section")
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 16.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.categories),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("Categories Section Lazy Row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(categories) { index, _ ->
                    when (index) {
                        0 -> {
                            HomeCategoryItem(
                                modifier = Modifier.padding(start = 16.dp),
                                category = categories[index],
                                onClick = { onClick(it) },
                                isSelected = selectedCategory == categories[index].categoryId
                            )
                        }
                        categories.lastIndex -> {
                            HomeCategoryItem(
                                modifier = Modifier.padding(end = 16.dp),
                                category = categories[index],
                                onClick = { onClick(it) },
                                isSelected = selectedCategory == categories[index].categoryId
                            )
                        }
                        else -> {
                            HomeCategoryItem(
                                category = categories[index],
                                onClick = { onClick(it) },
                                isSelected = selectedCategory == categories[index].categoryId
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
fun CategoriesSectionPreview() {
    RecipeAppTheme {
        val categories = listOf(
            Category("Appetizer",""),
            Category("Chicken",""),
            Category("Dinner",""),
            Category("Soup",""),
            Category("Stew","")
        )

        CategoriesSection(
            categories = categories,
            selectedCategory = "",
            onClick = {}
        )
    }
}