package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ShoppingListIngredientItem(
    modifier: Modifier = Modifier,
    note: String = "Note",
    isChecked: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageItem(
            modifier = modifier.size(40.dp),
            imageUrl = ""
        )

        Column(
            modifier = modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.ingredient),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 4.dp)
                )

                Text(
                    text = stringResource(id = R.string.quantity),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light,
                    maxLines = 1
                )
            }

            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = modifier.fillMaxWidth()
            )
        }

        Checkbox(
            checked = isChecked,
            onCheckedChange = {}
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
fun ShoppingListIngredientItemPreview() {
    RecipeAppTheme {
        Surface {
            ShoppingListIngredientItem()
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
fun ShoppingListIngredientItemPreviewChecked() {
    RecipeAppTheme {
        Surface {
            ShoppingListIngredientItem(
                isChecked = true
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
fun ShoppingListIngredientItemPreviewLongNote() {
    RecipeAppTheme {
        Surface {
            ShoppingListIngredientItem(
                note = "This is a very, very long note to see how the composable looks in this state."
            )
        }
    }
}