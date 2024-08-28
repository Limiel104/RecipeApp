package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ShoppingListCategoryItem(
    modifier: Modifier = Modifier,
    categoryName: String,
    ingredients: Map<Ingredient, Quantity>
) {
    var i = 0

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(bottom = 8.dp)
            )

            Column() {
                for (ingredient in ingredients) {
                    ShoppingListIngredientItem(
                        ingredient = ingredient.key,
                        quantity = ingredient.value
                    )

                    i += 1
                    if (i<ingredients.size)
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
fun ShoppingListCategoryItemPreview() {
    RecipeAppTheme {
        ShoppingListCategoryItem(
            categoryName = "Category Name",
            ingredients = getIngredientsWithQuantity()
        )
    }
}