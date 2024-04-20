package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarItem(
    modifier: Modifier = Modifier,
    query: String,
    isSearchActive: Boolean,
    onQueryChange: (String) -> Unit,
    onActiveChange: () -> Unit,
    onSearchClicked: () -> Unit,
    onClear: () -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { onSearchClicked() },
        active = isSearchActive,
        onActiveChange = { onActiveChange() },
        placeholder = { Text(text = "Search") },
        trailingIcon = {
            if(isSearchActive) {
                IconButton(onClick = { onClear() }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
            else {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .testTag("Search Bar")
    ) {}
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
fun SearchBarItemPreviewSearchIsNotActive() {
    RecipeAppTheme {
        SearchBarItem(
            query = "",
            isSearchActive = false,
            onQueryChange = {},
            onActiveChange = {},
            onSearchClicked = {},
            onClear = {}
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
fun SearchBarItemPreviewSearchIsActive() {
    RecipeAppTheme {
        SearchBarItem(
            query = "Search query",
            isSearchActive = true,
            onQueryChange = {},
            onActiveChange = {},
            onSearchClicked = {},
            onClear = {}
        )
    }
}