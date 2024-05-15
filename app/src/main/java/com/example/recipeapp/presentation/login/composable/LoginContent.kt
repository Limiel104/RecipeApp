package com.example.recipeapp.presentation.login.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    email: String,
    emailError: String?,
    password: String,
    passwordError: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onSignup: () -> Unit,
    onGoBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login)) },
                navigationIcon = {
                    IconButton(onClick = { onGoBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
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
                .testTag("Login Content"),
        ) {
            Column(
                modifier = modifier
                    .weight(4F)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { onEmailChange(it) },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    placeholder = { Text(text = stringResource(id = R.string.email)) },
                    supportingText = {
                        if (emailError != null) {
                            Text(text = emailError)
                        } },
                    isError = emailError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    modifier = modifier
                        .padding(bottom = 8.dp)
                        .testTag("Login email TF")
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
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    modifier = modifier
                        .padding(bottom = 16.dp)
                        .testTag("Login password TF")
                )

                Button(
                    modifier = modifier.testTag("Login button"),
                    onClick = { onLogin() }
                ) {
                    Text(text = stringResource(id = R.string.login))
                }
            }

            Column(
                modifier = modifier.weight(1F),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = R.string.no_account_text))

                    Spacer(modifier = modifier.width(5.dp))

                    TextButton(
                        onClick = { onSignup() }
                    ) {
                        Text(text = stringResource(id = R.string.signup),)
                    }
                }
            }
        }
    }

    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("Login CPI"),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
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
fun LoginContentPreview() {
    RecipeAppTheme() {
        LoginContent(
            email = "email@wp.com",
            emailError = null,
            password = "abcdef2+A",
            passwordError = null,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onSignup = {},
            onGoBack = {}
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
fun LoginLoginContentPreviewErrorsDisplayed() {
    RecipeAppTheme() {
        LoginContent(
            email = "email@wp.com",
            emailError = "Email can't be empty",
            password = "abcdef2+A",
            passwordError = "Password can't be empty",
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onSignup = {},
            onGoBack = {}
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
fun LoginContentPreviewCPI() {
    RecipeAppTheme() {
        LoginContent(
            email = "email@wp.com",
            emailError = null,
            password = "abcdef2+A",
            passwordError = null,
            isLoading = true,
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onSignup = {},
            onGoBack = {}
        )
    }
}