package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.FillWidth
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.ic_image)
                .build(),
            contentDescription = "IMAGE",
            fallback = painterResource(R.drawable.ic_image),
            error = painterResource(R.drawable.ic_image),
            contentScale = contentScale
        )
    }
}

@Preview(
    name = "Light Mode - 100.dp",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode - 100.dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ImageItemPreview100dp() {
    RecipeAppTheme {
        Surface {
            ImageItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {},
                imageUrl = ""
            )
        }
    }
}

@Preview(
    name = "Light Mode - 40.dp",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode - 40.dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ImageItemPreview40dp() {
    RecipeAppTheme {
        Surface {
            ImageItem(
                modifier = Modifier.size(40.dp),
                imageUrl = ""
            )
        }
    }
}