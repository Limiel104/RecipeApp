package com.example.recipeapp.presentation.account.composable

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.util.RecipeOrder
import com.example.recipeapp.presentation.account.AccountState
import com.example.recipeapp.presentation.common.composable.RecipeItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    uiState: AccountState,
    onAddRecipe: () -> Unit,
    onRecipeSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onSortRecipes: (RecipeOrder) -> Unit,
    onEditButtonClicked: () -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogSave: () -> Unit
) {
    Scaffold(
        modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = uiState.name) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEditButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit button"
                        )
                    }

                    IconButton(onClick = { onLogout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = "Logout button"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddRecipe() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add recipe"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("Account Content")
        ) {
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recipes",
                        style = MaterialTheme.typography.titleMedium
                    )

                    TextButton(
                        onClick = {
                            onSortRecipes(
                                if(uiState.recipesOrder == RecipeOrder.DateDescending)
                                    RecipeOrder.DateAscending
                                else
                                    RecipeOrder.DateDescending
                            )
                        }
                    ) {
                        Text(
                            text = if(uiState.recipesOrder == RecipeOrder.DateDescending)
                                stringResource(id = R.string.newest)
                            else
                                stringResource(id = R.string.oldest),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            itemsIndexed(uiState.recipes) { _, recipe ->
                RecipeItem(
                    recipe = recipe,
                    cardHorizontalPadding = 16.dp,
                    cardBottomPadding = 16.dp,
                    onBookmark = {},
                    onClick = { onRecipeSelected(recipe.recipeId) }
                )
            }
        }

        if(uiState.isEditDialogActivated) {
            EditDialog(
                name = uiState.editName,
                nameError = uiState.nameError,
                password = uiState.password,
                passwordError = uiState.passwordError,
                confirmPassword = uiState.confirmPassword,
                confirmPasswordError = uiState.confirmPasswordError,
                onNameChange = { onNameChange(it) },
                onPasswordChange = { onPasswordChange(it) },
                onConfirmPasswordChange = { onConfirmPasswordChange(it) },
                onDismiss = { onDialogDismiss() },
                onSave = { onDialogSave() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun AccountContentPreview() {
    RecipeAppTheme {
        AccountContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            uiState = AccountState(),
            onAddRecipe = {},
            onRecipeSelected = {},
            onLogout = {},
            onSortRecipes = {},
            onEditButtonClicked = {},
            onNameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onDialogDismiss = {},
            onDialogSave = {}
        )
    }
}