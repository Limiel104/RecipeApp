package com.example.recipeapp.presentation.common.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun UserNotLoggedInContent(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onSignup: () -> Unit
) {
    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("User not logged in Content")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.not_logged_in),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource(id = R.string.login_or_signup),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = modifier.testTag("Login button"),
                    onClick = { onLogin() }
                ) {
                    Text(text = stringResource(id = R.string.login))
                }

                Button(
                    modifier = modifier.testTag("Signup button"),
                    onClick = { onSignup() }
                ) {
                    Text(text = stringResource(id = R.string.signup))
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
fun UserNotLoggedInContentPreview() {
    RecipeAppTheme {
        UserNotLoggedInContent(
            onLogin = {},
            onSignup = {}
        )
    }
}