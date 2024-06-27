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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeIngredientItem(
    modifier: Modifier = Modifier,
    ingredient: Ingredient,
    quantity: String,
    dragIndex: String,
    isReorderModeActivated: Boolean,
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
                    modifier.clickable { onClick(ingredient.ingredientId) }
                }
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                if(dragIndex == ingredient.ingredientId) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.background
            )
            .testTag("Recipe Ingredient Item ${ingredient.name}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageItem(
            modifier = modifier.size(40.dp),
            imageUrl = ingredient.imageUrl
        )

        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = quantity,
            style = MaterialTheme.typography.bodySmall,
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
fun RecipeIngredientItemPreview() {
    RecipeAppTheme {
        Surface {
            RecipeIngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                dragIndex = "",
                isReorderModeActivated = false,
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
fun RecipeIngredientItemDragIndexEqualToItemId() {
    RecipeAppTheme {
        Surface {
            RecipeIngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                dragIndex = "ingredientId",
                isReorderModeActivated = false,
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
fun RecipeIngredientItemPreviewReorderActivated() {
    RecipeAppTheme {
        Surface {
            RecipeIngredientItem(
                ingredient = Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                quantity = "200 g",
                dragIndex = "",
                isReorderModeActivated = true,
                onClick = {}
            )
        }
    }
}