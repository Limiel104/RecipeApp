package com.example.recipeapp.presentation.login.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController
) {
    val email = "email"
    val emailError = null
    val password = "password"
    val passwordError = null
    val isLoading = false
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        }
    }

    LoginContent(
        email = email,
        emailError = emailError,
        password = password,
        passwordError = passwordError,
        isLoading = isLoading,
        onEmailChange = {  },
        onPasswordChange = {  },
        onLogin = {  },
        onSignup = {  },
        onGoBack = {  }
    )
}