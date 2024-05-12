package com.example.recipeapp.presentation.login.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.presentation.login.LoginEvent
import com.example.recipeapp.presentation.login.LoginUiEvent
import com.example.recipeapp.presentation.login.LoginViewModel
import com.example.recipeapp.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email = viewModel.loginState.value.email
    val emailError = viewModel.loginState.value.emailError
    val password = viewModel.loginState.value.password
    val passwordError = viewModel.loginState.value.passwordError
    val lastDestination = viewModel.loginState.value.lastDestination
    val isLoading = viewModel.loginState.value.isLoading
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loginUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Login Screen LE")
                when(event) {
                    is LoginUiEvent.ShowErrorMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                    LoginUiEvent.NavigateToSignup -> {
                        navController.navigate(Screen.SignupScreen.route + "lastDestination=" + lastDestination)
                    }
                    LoginUiEvent.NavigateBack -> {
                        navController.popBackStack()
                    }
                    LoginUiEvent.Login -> {
                        navController.navigate(lastDestination) {
                            popUpTo(lastDestination) { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    LoginContent(
        email = email,
        emailError = emailError,
        password = password,
        passwordError = passwordError,
        isLoading = isLoading,
        onEmailChange = { viewModel.onEvent(LoginEvent.EnteredEmail(it)) },
        onPasswordChange = { viewModel.onEvent(LoginEvent.EnteredPassword(it)) },
        onLogin = { viewModel.onEvent(LoginEvent.OnLogin) },
        onSignup = { viewModel.onEvent(LoginEvent.OnSignup) },
        onGoBack = { viewModel.onEvent(LoginEvent.OnGoBack) }
    )
}