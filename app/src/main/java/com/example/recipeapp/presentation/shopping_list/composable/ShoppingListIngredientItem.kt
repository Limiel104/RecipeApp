package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ShoppingListIngredientItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(R.drawable.ic_image)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_image)
                    .build(),
                contentDescription = "IMAGE",
                fallback = painterResource(R.drawable.ic_image),
                error = painterResource(R.drawable.ic_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.clickable {}
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row() {
                Text(
                    text = "Ingredient",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Quantity",
                    fontWeight = FontWeight.Light,
                )
            }

            Text(
                text = "Note",
                fontWeight = FontWeight.Light,
            )
        }

        Checkbox(
            checked = false,
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