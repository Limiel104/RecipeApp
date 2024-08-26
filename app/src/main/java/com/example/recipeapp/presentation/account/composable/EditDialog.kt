package com.example.recipeapp.presentation.account.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    modifier: Modifier = Modifier,
    name: String,
    nameError: String?,
    password: String,
    passwordError: String?,
    confirmPassword: String,
    confirmPasswordError: String?,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Edit Account") },
                    navigationIcon = {
                        IconButton(onClick = { onDismiss() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear button"
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { onSave() },
                            modifier = modifier.testTag("Save button")
                        ) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .testTag("Edit dialog")
            ) {
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
                        .padding(bottom = 24.dp)
                        .testTag("Account name TF")
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { onPasswordChange(it) },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    placeholder = { Text(text = stringResource(id = R.string.password)) },
                    supportingText = {
                        if (passwordError != null) {
                            Text(text = passwordError)
                        } },
                    isError = passwordError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .testTag("Account password TF")
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { onConfirmPasswordChange(it) },
                    label = { Text(text = stringResource(id = R.string.confirm_password)) },
                    placeholder = { Text(text = stringResource(id = R.string.confirm_password)) },
                    supportingText = {
                        if (confirmPasswordError != null) {
                            Text(text = confirmPasswordError)
                        } },
                    isError = confirmPasswordError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .testTag("Account confirm password TF")
                )
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
fun EditDialogPreview() {
    RecipeAppTheme {
        EditDialog(
            name = "Name",
            nameError = null,
            password = "Qwerty!1",
            passwordError = null,
            confirmPassword = "Qwerty!1",
            confirmPasswordError = null,
            onNameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onDismiss = {},
            onSave = {}
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
fun EditDialogPreviewErrors() {
    RecipeAppTheme {
        EditDialog(
            name = "Name",
            nameError = "Field is too short",
            password = "Qwerty!",
            passwordError = "Password should have at least one digit",
            confirmPassword = "Qwerty!1",
            confirmPasswordError = "Passwords don't mach",
            onNameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onDismiss = {},
            onSave = {}
        )
    }
}