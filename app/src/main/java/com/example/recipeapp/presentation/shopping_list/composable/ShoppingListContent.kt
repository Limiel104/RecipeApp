package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.presentation.common.composable.QuantityPicker
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.presentation.shopping_list.ShoppingListState
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListContent(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    modalBottomSheetState: SheetState,
    uiState: ShoppingListState,
    onIngredientSuggestionClick: (Ingredient) -> Unit,
    onDropDownMenuExpandedChange: () -> Unit,
    onIngredientChange: (String) -> Unit,
    onAddIngredientsDialogDismiss: () -> Unit,
    onAddIngredientsSave: () -> Unit,
    onAddButtonClick: () -> Unit,
    onIngredientClick: (String) -> Unit,
    onSelectedWholeQuantity: (String) -> Unit,
    onSelectedDecimalQuantity: (String) -> Unit,
    onSelectedTypeQuantity: (String) -> Unit,
    onQuantityPickerDismiss: () -> Unit,
    onQuantityPickerSave: () -> Unit,
    onCheckedChange: (Ingredient) -> Unit,
    onMenuButtonClicked: () -> Unit,
    onMenuDismissed: () -> Unit,
    onOpenRenameShoppingListDialog: () -> Unit,
    onRenameShoppingListDialogDismissed: () -> Unit,
    onDeleteAllIngredients: () -> Unit,
    onDeleteShoppingList: () -> Unit,
    onOpenOtherShoppingListsMenu: () -> Unit,
    onOtherShoppingListsMenuDismiss: () -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.shopping_list)) },
                actions = {
                    IconButton(onClick = { onMenuButtonClicked() }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu button"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add button"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .testTag("Shopping List Content")
        ) {
            item {
                Text(
                    text = uiState.shoppingListIngredients.keys.size.toString()
                            + if(uiState.shoppingListIngredients.keys.size == 1) " item" else " items",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light,
                    modifier = modifier
                        .padding(bottom = 8.dp)
                        .padding(start = 16.dp)
                )
            }

            val categories = uiState.shoppingListIngredients.keys.groupBy { it.category }.keys.toList()
            itemsIndexed(categories) { _, category ->
                ShoppingListCategoryItem(
                    categoryName = category,
                    ingredients = uiState.shoppingListIngredients.filter { it.key.category == category },
                    checkedIngredients = uiState.checkedIngredients,
                    onCheckedChange = { onCheckedChange(it) },
                    onIngredientClick = { onIngredientClick(it) }
                )
            }
        }

        if(uiState.isAddIngredientsDialogOpened) {
            AddIngredientsDialog(
                ingredient = uiState.ingredient,
                isDropDownMenuExpanded = uiState.isDropDownMenuExpanded,
                ingredientsToSelect = uiState.ingredientsToSelect,
                selectedIngredients = uiState.selectedIngredients,
                onIngredientSuggestionClick = { onIngredientSuggestionClick(it) },
                onDropDownMenuExpandedChange = { onDropDownMenuExpandedChange() },
                onIngredientChange = { onIngredientChange(it) },
                onDismiss = { onAddIngredientsDialogDismiss() },
                onSave = { onAddIngredientsSave() }
            )
        }

        if(uiState.isQuantityBottomSheetOpened) {
            QuantityPicker(
                modalSheetState = modalBottomSheetState,
                selectedWholeQuantity = uiState.selectedWholeQuantity,
                selectedDecimalQuantity = uiState.selectedDecimalQuantity,
                selectedTypeQuantity = uiState.selectedTypeQuantity,
                onSelectedWholeQuantity = { onSelectedWholeQuantity(it) },
                onSelectedDecimalQuantity = { onSelectedDecimalQuantity(it) },
                onSelectedTypeQuantity = { onSelectedTypeQuantity(it) },
                onDismiss = { onQuantityPickerDismiss() },
                onSave = { onQuantityPickerSave() }
            )
        }

        if(uiState.isMenuOpened) {
            ShoppingListMenu(
                modalSheetState = modalBottomSheetState,
                onRename = { onOpenRenameShoppingListDialog() },
                onDeleteAllIngredients = { onDeleteAllIngredients() },
                onDeleteList = { onDeleteShoppingList() },
                onOpenOtherListsMenu = { onOpenOtherShoppingListsMenu() },
                onDismiss = { onMenuDismissed() }
            )
        }

        if(uiState.isOtherShoppingListsMenuOpened) {
            OtherShoppingListsMenu(
                modalSheetState = modalBottomSheetState,
                shoppingLists = uiState.userShoppingLists,
                onDismiss = { onOtherShoppingListsMenuDismiss() },
                onClick = {  }
            )
        }

        if(uiState.isRenameShoppingListDialogOpened) {
            RenameShoppingListDialog(
                name = uiState.displayedShoppingList.name,
                nameError = null,
                onNameChange = {},
                onDismiss = { onRenameShoppingListDialogDismissed() },
                onSave = {  }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ShoppingListContentPreview() {
    RecipeAppTheme {
        ShoppingListContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = ShoppingListState(shoppingListIngredients = getIngredientsWithQuantity()),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onAddIngredientsDialogDismiss = {},
            onAddIngredientsSave = {},
            onAddButtonClick = {},
            onIngredientClick = {},
            onSelectedWholeQuantity = {},
            onSelectedDecimalQuantity = {},
            onSelectedTypeQuantity = {},
            onQuantityPickerDismiss = {},
            onQuantityPickerSave = {},
            onCheckedChange = {},
            onMenuButtonClicked = {},
            onMenuDismissed = {},
            onOpenRenameShoppingListDialog = {},
            onRenameShoppingListDialogDismissed = {},
            onDeleteAllIngredients = {},
            onDeleteShoppingList = {},
            onOpenOtherShoppingListsMenu = {},
            onOtherShoppingListsMenuDismiss = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ShoppingListContentPreviewDialog() {
    RecipeAppTheme {
        ShoppingListContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = ShoppingListState(isAddIngredientsDialogOpened = true),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onAddIngredientsDialogDismiss = {},
            onAddIngredientsSave = {},
            onAddButtonClick = {},
            onIngredientClick = {},
            onSelectedWholeQuantity = {},
            onSelectedDecimalQuantity = {},
            onSelectedTypeQuantity = {},
            onQuantityPickerDismiss = {},
            onQuantityPickerSave = {},
            onCheckedChange = {},
            onMenuButtonClicked = {},
            onMenuDismissed = {},
            onOpenRenameShoppingListDialog = {},
            onRenameShoppingListDialogDismissed = {},
            onDeleteAllIngredients = {},
            onDeleteShoppingList = {},
            onOpenOtherShoppingListsMenu = {},
            onOtherShoppingListsMenuDismiss = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ShoppingListContentPreviewOneItem() {
    RecipeAppTheme {
        ShoppingListContent(
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = ShoppingListState(shoppingListIngredients = mapOf(
                Pair(
                    Ingredient(
                        ingredientId = "ingredientId",
                        name = "Ingredient Name",
                        imageUrl = "imageUrl",
                        category = "category"
                    ),
                "200.0 g"
            ))),
            onIngredientSuggestionClick = {},
            onDropDownMenuExpandedChange = {},
            onIngredientChange = {},
            onAddIngredientsDialogDismiss = {},
            onAddIngredientsSave = {},
            onAddButtonClick = {},
            onIngredientClick = {},
            onSelectedWholeQuantity = {},
            onSelectedDecimalQuantity = {},
            onSelectedTypeQuantity = {},
            onQuantityPickerDismiss = {},
            onQuantityPickerSave = {},
            onCheckedChange = {},
            onMenuButtonClicked = {},
            onMenuDismissed = {},
            onOpenRenameShoppingListDialog = {},
            onRenameShoppingListDialogDismissed = {},
            onDeleteAllIngredients = {},
            onDeleteShoppingList = {},
            onOpenOtherShoppingListsMenu = {},
            onOtherShoppingListsMenuDismiss = {}
        )
    }
}