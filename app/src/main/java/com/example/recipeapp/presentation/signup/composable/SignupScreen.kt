package com.example.recipeapp.presentation.signup.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController

@Composable
fun SignupScreen(
    navController: NavController
) {
    val email = "email"
    val emailError = null
    val password = "password"
    val passwordError = null
    val confirmPassword = "password"
    val confirmPasswordError = null
    val name = "name"
    val nameError = null
    val isLoading = false
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        }
    }

    SignupContent(
        email = email,
        emailError = emailError,
        password = password,
        passwordError = passwordError,
        confirmPassword = confirmPassword,
        confirmPasswordError = confirmPasswordError,
        name = name,
        nameError = nameError,
        isLoading = isLoading,
        onEmailChange = {  },
        onPasswordChange = {  },
        onConfirmPasswordChange = {  },
        onNameChange = {  },
        onSignup = {  },
        onGoBack = {  }
    )
}