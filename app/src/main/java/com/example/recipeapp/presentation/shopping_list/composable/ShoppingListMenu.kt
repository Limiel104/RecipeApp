@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.presentation.common.composable.MenuItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ShoppingListMenu(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    onRename: () -> Unit,
    onDeleteAllIngredients: () -> Unit,
    onDeleteList: () -> Unit,
    onAdd: () -> Unit,
    onOpenOtherListsMenu: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("Shopping list menu"),
        ) {
            MenuItem(
                icon = Icons.Default.Edit,
                text = "Rename",
                onClick = { onRename() }
            )

            MenuItem(
                icon = Icons.Rounded.Clear,
                text = "Remove all items",
                onClick = { onDeleteAllIngredients() }
            )

            MenuItem(
                icon = Icons.Default.Delete,
                text = "Delete list",
                onClick = { onDeleteList() }
            )

            MenuItem(
                icon = Icons.Default.Edit,
                text = "Add new list",
                onClick = { onAdd() }
            )

            MenuItem(
                icon = Icons.Default.Menu,
                text = "View other lists",
                onClick = { onOpenOtherListsMenu() },
                modifier = modifier.padding(bottom = 36.dp)
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
fun ShoppingListMenuPreview() {
    RecipeAppTheme {
        Surface {
            ShoppingListMenu(
                modalSheetState = rememberModalBottomSheetState(),
                onRename = {},
                onDeleteAllIngredients = {},
                onDeleteList = {},
                onAdd = {},
                onOpenOtherListsMenu = {},
                onDismiss = {}
            )
        }
    }
}