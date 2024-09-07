package com.example.recipeapp.presentation.common.composable

import android.content.ClipData
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientItem(
    modifier: Modifier = Modifier,
    ingredient: Ingredient,
    quantity: String,
    dragIndex: String = "",
    color: Color = MaterialTheme.colorScheme.background,
    isReorderModeActivated: Boolean = false,
    isShoppingListModeActivated: Boolean = false,
    isChecked: Boolean = false,
    onCheckedChange: (Ingredient) -> Unit,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .then(
                if (isReorderModeActivated) {
                    modifier.dragAndDropSource {
                        detectTapGestures(
                            onLongPress = {
                                startTransfer(
                                    DragAndDropTransferData(
                                        clipData = ClipData.newPlainText("ingredientIndex", ingredient.ingredientId)
                                    )
                                )
                            }
                        )
                    }
                } else {
                    if(!isChecked) modifier.clickable { onClick(ingredient.ingredientId) } else modifier
                }
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                if(dragIndex == ingredient.ingredientId) MaterialTheme.colorScheme.secondary
                else color
            )
            .testTag("Ingredient Item ${ingredient.name}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageItem(
            modifier = modifier.size(40.dp),
            imageUrl = ingredient.imageUrl
        )

        Text(
            text = ingredient.name,
            style =
                if(isChecked)
                    MaterialTheme.typography.bodyMedium.plus(TextStyle(textDecoration = TextDecoration.LineThrough))
                else MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = quantity,
            style = if(isChecked)
                MaterialTheme.typography.bodySmall.plus(TextStyle(textDecoration = TextDecoration.LineThrough))
            else MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .weight(1F)
                .padding(horizontal = 8.dp)
        )

        if(isReorderModeActivated) {
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "Drag handle icon",
                modifier = modifier
            )
        }

        if(isShoppingListModeActivated) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(ingredient) }
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
fun IngredientItemPreview() {
    RecipeAppTheme {
        Surface {
            IngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                onCheckedChange = {},
                onClick = {}
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
fun IngredientItemDragIndexEqualToItemId() {
    RecipeAppTheme {
        Surface {
            IngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                dragIndex = "ingredientId",
                onCheckedChange = {},
                onClick = {}
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
fun IngredientItemPreviewReorderActivated() {
    RecipeAppTheme {
        Surface {
            IngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                isReorderModeActivated = true,
                onCheckedChange = {},
                onClick = {}
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
fun IngredientItemPreviewCheckboxFalse() {
    RecipeAppTheme {
        Surface {
            IngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                isShoppingListModeActivated = true,
                onCheckedChange = {},
                onClick = {}
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
fun IngredientItemPreviewCheckboxTrue() {
    RecipeAppTheme {
        Surface {
            IngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                isShoppingListModeActivated = true,
                isChecked = true,
                onCheckedChange = {},
                onClick = {}
            )
        }
    }
}