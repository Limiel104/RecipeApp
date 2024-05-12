package com.example.recipeapp.presentation.signup.composable

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
import com.example.recipeapp.presentation.signup.SignupEvent
import com.example.recipeapp.presentation.signup.SignupUiEvent
import com.example.recipeapp.presentation.signup.SignupViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val email = viewModel.signupState.value.email
    val emailError = viewModel.signupState.value.emailError
    val password = viewModel.signupState.value.password
    val passwordError = viewModel.signupState.value.passwordError
    val confirmPassword = viewModel.signupState.value.confirmPassword
    val confirmPasswordError = viewModel.signupState.value.confirmPasswordError
    val name = viewModel.signupState.value.name
    val nameError = viewModel.signupState.value.nameError
    val lastDestination = viewModel.signupState.value.lastDestination
    val isLoading = viewModel.signupState.value.isLoading
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.signupUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Signup Screen LE")
                when(event) {
                    is SignupUiEvent.ShowErrorMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                    is SignupUiEvent.Signup -> {
                        navController.navigate(lastDestination) {
                            popUpTo(lastDestination) { inclusive = true }
                        }
                    }
                    is SignupUiEvent.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }
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
        onEmailChange = { viewModel.onEvent(SignupEvent.EnteredEmail(it)) },
        onPasswordChange = { viewModel.onEvent(SignupEvent.EnteredPassword(it)) },
        onConfirmPasswordChange = { viewModel.onEvent(SignupEvent.EnteredConfirmPassword(it)) },
        onNameChange = { viewModel.onEvent(SignupEvent.EnteredName(it)) },
        onSignup = { viewModel.onEvent(SignupEvent.OnSignup) },
        onGoBack = { viewModel.onEvent(SignupEvent.OnGoBack) }
    )
}