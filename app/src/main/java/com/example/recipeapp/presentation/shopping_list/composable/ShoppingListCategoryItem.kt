@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.presentation.common.composable.IngredientItem
import com.example.recipeapp.presentation.common.getIngredientsWithBoolean
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ShoppingListCategoryItem(
    modifier: Modifier = Modifier,
    categoryName: String,
    ingredients: Map<Ingredient, Quantity>,
    checkedIngredients: Map<Ingredient, Boolean>,
    onCheckedChange: (Ingredient) -> Unit,
    onIngredientClick: (String) -> Unit,
    onSwipeToDelete: (Ingredient) -> Unit
) {
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
                for(ingredient in ingredients) {
                    checkedIngredients[ingredient.key]?.let {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    onSwipeToDelete(ingredient.key)
                                    false
                                } else true
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color =
                                    if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                        Color.Red
                                    } else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete icon",
                                        tint = if(dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.White
                                                else Color.Transparent
                                    )
                                }
                            },
                            content = {
                                Log.i("TAG","Swipe $ingredient")

                                IngredientItem(
                                    ingredient = ingredient.key,
                                    quantity = ingredient.value,
                                    color = Color.Transparent,
                                    isShoppingListModeActivated = true,
                                    isChecked = it,
                                    onCheckedChange = { onCheckedChange(it) },
                                    onClick = { onIngredientClick(it) }
                                )
                            }
                        )
                    }

                    if(ingredients.keys.indexOf(ingredient.key) != ingredients.keys.toList().lastIndex)
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
            ingredients = getIngredientsWithQuantity(),
            checkedIngredients = getIngredientsWithBoolean(),
            onCheckedChange = {},
            onIngredientClick = {},
            onSwipeToDelete = {}
        )
    }
}