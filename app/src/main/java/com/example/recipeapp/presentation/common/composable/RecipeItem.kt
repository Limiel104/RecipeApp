package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.Recipe
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    cardHorizontalPadding: Dp = 0.dp,
    cardBottomPadding: Dp = 0.dp,
    isBookmarkVisible: Boolean = false,
    onBookmark: (String) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = cardHorizontalPadding)
            .padding(bottom = cardBottomPadding)
            .clickable { onClick() }
            .testTag("Recipe ${recipe.name}"),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ImageItem(
                modifier = modifier.fillMaxSize(),
                imageUrl = recipe.imageUrl
            )

            if(isBookmarkVisible) {
                IconButton(onClick = { onBookmark(recipe.recipeId) }) {
                    Icon(
                        imageVector = Icons.Outlined.Bookmark,
                        contentDescription = "Icon button"
                    )
                }
            }

            Text(
                text = recipe.name,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}

private fun getRecipe(name: String = "Recipe Name"): Recipe {
    return Recipe(
        recipeId = "recipeId",
        name = name,
        prepTime = "40 min",
        servings = 4,
        description = "Recipe description",
        isVegetarian = true,
        isVegan = false,
        imageUrl = "imageUrl",
        createdBy = "userId",
        categories = emptyList(),
        date = 123234
    )
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
fun RecipeItemPreview() {
    RecipeAppTheme {
        RecipeItem(
            recipe = getRecipe(),
            isBookmarkVisible = true,
            onBookmark = {},
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
fun RecipeItemPreviewLongTitle() {
    RecipeAppTheme {
        RecipeItem(
            recipe = getRecipe("Very very long title of the recipe for this composable. This is another line in the title. Let's make it even longer so it overflows"),
            isBookmarkVisible = true,
            onBookmark = {},
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
fun TopCategoriesSectionPreviewWithCardPadding() {
    RecipeAppTheme {
        Surface {
            RecipeItem(
                recipe = getRecipe(),
                cardHorizontalPadding = 16.dp,
                cardBottomPadding = 16.dp,
                onBookmark = {},
                onClick = {}
            )
        }
    }
}