package com.example.recipeapp.presentation.signup.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupContent(
    modifier: Modifier = Modifier,
    email: String,
    emailError: String?,
    password: String,
    passwordError: String?,
    confirmPassword: String,
    confirmPasswordError: String?,
    name: String,
    nameError: String?,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onSignup: () -> Unit,
    onGoBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.signup)) },
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
                .testTag("Signup Content"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = { Text(text = stringResource(id = R.string.email)) },
                placeholder = { Text(text = stringResource(id = R.string.email)
                ) },
                supportingText = {
                    if(emailError != null) {
                        Text(text = emailError)
                    } },
                isError = emailError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .testTag("Signup email TF")
            )

            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.password)) },
                placeholder = { Text(text = stringResource(id = R.string.password)
                ) },
                supportingText = {
                    if(passwordError != null) {
                        Text(text = passwordError)
                    } },
                isError = passwordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .testTag("Signup password TF")
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { onConfirmPasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                placeholder = { Text(text = stringResource(id = R.string.confirm_password)
                ) },
                supportingText = {
                    if(confirmPasswordError != null) {
                        Text(text = confirmPasswordError)
                    } },
                isError = confirmPasswordError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .testTag("Signup confirm password TF")
            )

            OutlinedTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                label = { Text(text = stringResource(id = R.string.name)) },
                placeholder = { Text(text = stringResource(id = R.string.name)
                ) },
                supportingText = {
                    if(nameError != null) {
                        Text(text = nameError)
                    } },
                isError = nameError != null,
                singleLine = true,
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .testTag("Signup name TF")
            )

            Button(
                modifier = modifier.testTag("Signup button"),
                onClick = { onSignup() }
            ) {
                Text(text =stringResource(id = R.string.signup))
            }
        }
    }

    if(isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("Signup CPI"),
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
fun SignupContentPreview() {
    RecipeAppTheme() {
        SignupContent(
            email = "email@wp.com",
            emailError = null,
            password = "abcdef2+A",
            passwordError = null,
            confirmPassword = "abcdef2+A",
            confirmPasswordError = null,
            name = "John",
            nameError = null,
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onNameChange = {},
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
fun SignupContentPreviewErrorsDisplayed() {
    RecipeAppTheme() {
        SignupContent(
            email = "email@wp.com",
            emailError = "Email can't be empty",
            password = "abcdef2+A",
            passwordError = "Password can't be empty",
            confirmPassword = "abcdeff2+A",
            confirmPasswordError = "Passwords don't mach",
            name = "John",
            nameError = "Name can't be empty",
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onNameChange = {},
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
fun SignupContentPreviewCPI() {
    RecipeAppTheme() {
        SignupContent(
            email = "email@wp.com",
            emailError = null,
            password = "abcdef2+A",
            passwordError = null,
            confirmPassword = "abcdeff2+A",
            confirmPasswordError = null,
            name = "John",
            nameError = null,
            isLoading = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onNameChange = {},
            onSignup = {},
            onGoBack = {}
        )
    }
}