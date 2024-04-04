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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    title: String = "Title of the recipe",
    cardHorizontalPadding: Dp = 0.dp,
    cardBottomPadding: Dp = 0.dp,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = cardHorizontalPadding)
            .padding(bottom = cardBottomPadding)
            .clickable { onClick() }
            .testTag("Recipe $title"),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ImageItem(modifier = modifier.fillMaxSize())

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Bookmark,
                    contentDescription = "Icon button"
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
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
fun RecipeItemPreview() {
    RecipeAppTheme {
        RecipeItem {}
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
            title = "Very very long title of the recipe for this composable. This is another line in the title. Let's make it even longer so it overflows",
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
                cardHorizontalPadding = 16.dp,
                cardBottomPadding = 16.dp,
                onClick = {}
            )
        }
    }
}