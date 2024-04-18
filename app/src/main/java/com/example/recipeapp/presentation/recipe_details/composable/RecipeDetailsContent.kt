package com.example.recipeapp.presentation.recipe_details.composable

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.ImageItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    secondaryTabState: Int,
    tabTitleList: List<String>,
    onTabChanged: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "Bookmark recipe button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .testTag("Recipe Details Content")
        ) {
            Text(
                text = "Recipe name that is very long long long long",
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )

            ImageItem(
                modifier = modifier
                    .fillMaxWidth()
                    .height(180.dp),
                imageUrl = ""
            )

            Column() {
                SecondaryTabRow(selectedTabIndex = secondaryTabState) {
                    tabTitleList.forEachIndexed { index, title ->
                        Tab(
                            selected = secondaryTabState == index,
                            onClick = { onTabChanged() },
                            modifier = Modifier.testTag("$title Tab Title"),
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
            }

            if (secondaryTabState == 0) {
                IngredientsTab()
            }
            else {
                DescriptionTab()
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
fun RecipeDetailsContentPreviewIngredientsTab() {
    RecipeAppTheme {
        RecipeDetailsContent(
            secondaryTabState = 0,
            scrollState = rememberScrollState(),
            tabTitleList = listOf("Ingredients","Description"),
            onTabChanged = {}
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
fun RecipeDetailsContentPreviewDescriptionTab() {
    RecipeAppTheme {
        RecipeDetailsContent(
            secondaryTabState = 1,
            scrollState = rememberScrollState(),
            tabTitleList = listOf("Ingredients","Description"),
            onTabChanged = {}
        )
    }
}