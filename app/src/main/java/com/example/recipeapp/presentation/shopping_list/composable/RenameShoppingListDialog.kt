@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.shopping_list.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun RenameShoppingListDialog(
    modifier: Modifier = Modifier,
    name: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card() {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .testTag("Rename shopping list dialog")
            ) {
                Text(
                    text = "Rename list",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    label = { Text(text = stringResource(id = R.string.name)) },
                    placeholder = { Text(text = stringResource(id = R.string.name)) },
                    supportingText = {
                        if (nameError != null) {
                            Text(text = nameError)
                        } },
                    isError = nameError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .testTag("Rename shopping list TF")
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = "Dismiss")
                    }

                    TextButton(onClick = { onSave() }) {
                        Text(
                            text = "Confirm",
                            modifier = modifier.padding(start = 12.dp)
                        )
                    }
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
fun RenameShoppingListDialogPreview() {
    RecipeAppTheme {
        Surface {
            RenameShoppingListDialog(
                name = "Shopping List Name",
                nameError = null,
                onNameChange = {},
                onDismiss = {},
                onSave = {}
            )
        }
    }
}