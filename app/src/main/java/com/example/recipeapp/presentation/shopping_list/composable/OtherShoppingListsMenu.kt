@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.ShoppingList
import com.example.recipeapp.presentation.common.getShoppingLists
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun OtherShoppingListsMenu(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    shoppingLists: List<ShoppingList>,
    onDismiss: () -> Unit,
    onClick: (String) -> Unit
) {
    var i = 0

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
        modifier = modifier.testTag("Image picker")
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            for(shoppingList in shoppingLists) {
                i += 1

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .then(
                            if (i == shoppingLists.size) modifier.padding(bottom = 36.dp)
                            else modifier
                        )
                        .clickable { onClick(shoppingList.shoppingListId) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 12.dp),
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "List icon"
                    )

                    Text(
                        text = shoppingList.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
fun OtherShoppingListsPreview() {
    RecipeAppTheme {
        Surface {
            OtherShoppingListsMenu(
                modalSheetState = rememberModalBottomSheetState(),
                shoppingLists = getShoppingLists(),
                onDismiss = {},
                onClick = {}
            )
        }
    }
}