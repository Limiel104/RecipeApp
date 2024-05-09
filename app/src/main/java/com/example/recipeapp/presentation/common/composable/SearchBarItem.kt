package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.SearchSuggestion
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarItem(
    modifier: Modifier = Modifier,
    query: String,
    searchSuggestions: List<SearchSuggestion>,
    isSearchActive: Boolean,
    onQueryChange: (String) -> Unit,
    onActiveChange: () -> Unit,
    onSearchClicked: () -> Unit,
    onClear: () -> Unit,
    onSearchSuggestionClicked: (String) -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { onSearchClicked() },
        active = isSearchActive,
        onActiveChange = { onActiveChange() },
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        trailingIcon = {
            if(isSearchActive) {
                IconButton(onClick = { onClear() }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (!isSearchActive) 16.dp else 0.dp)
            .testTag("Search Bar")
    ) {
        searchSuggestions.forEach { searchSuggestion ->
            SearchSuggestionItem(
                text = searchSuggestion.text,
                onClick = { onSearchSuggestionClicked(searchSuggestion.text) }
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
fun SearchBarItemPreviewSearchIsNotActive() {
    RecipeAppTheme {
        SearchBarItem(
            query = "",
            searchSuggestions = emptyList(),
            isSearchActive = false,
            onQueryChange = {},
            onActiveChange = {},
            onSearchClicked = {},
            onClear = {},
            onSearchSuggestionClicked = {}
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
        val queryList = listOf(
            SearchSuggestion(1,"Query 1"),
            SearchSuggestion(2,"Query 2"),
            SearchSuggestion(3,"Query 3"),
            SearchSuggestion(4,"Query 4"),
            SearchSuggestion(5,"Query 5"),
            SearchSuggestion(6,"Query 6"),
            SearchSuggestion(7,"Query 7"),
        )

        SearchBarItem(
            query = "Search query",
            searchSuggestions = queryList,
            isSearchActive = true,
            onQueryChange = {},
            onActiveChange = {},
            onSearchClicked = {},
            onClear = {},
            onSearchSuggestionClicked = {}
        )
    }
}