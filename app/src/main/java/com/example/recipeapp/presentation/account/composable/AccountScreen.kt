package com.example.recipeapp.presentation.account.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.presentation.account.AccountEvent
import com.example.recipeapp.presentation.account.AccountUiEvent
import com.example.recipeapp.presentation.account.AccountViewModel
import com.example.recipeapp.presentation.common.composable.UserNotLoggedInContent
import com.example.recipeapp.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isUserLoggedIn = viewModel.accountState.value.isUserLoggedIn
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.accountUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Account Screen LE")
                when (event) {
                    is AccountUiEvent.ShowErrorMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                    is AccountUiEvent.NavigateToRecipeDetails -> {
                        navController.navigate(Screen.RecipeDetailsScreen.route + "recipeId=" + event.recipeId)
                    }
                    AccountUiEvent.NavigateToLogin -> {
                        navController.navigate(Screen.LoginScreen.route + "lastDestination=" + Screen.AccountScreen.route)
                    }
                    AccountUiEvent.NavigateToSignup -> {
                        navController.navigate(Screen.SignupScreen.route + "lastDestination=" + Screen.AccountScreen.route)
                    }
                    AccountUiEvent.NavigateToAddRecipe -> {
                        navController.navigate(Screen.AddRecipeScreen.route)
                    }
                }
            }
        }
    }

    if(isUserLoggedIn) {
        AccountContent(
            scrollBehavior = scrollBehavior,
            uiState = viewModel.accountState.value,
            onAddRecipe = { viewModel.onEvent(AccountEvent.OnAddRecipeButtonClicked) },
            onRecipeSelected = { viewModel.onEvent(AccountEvent.OnRecipeSelected(it)) },
            onLogout = { viewModel.onEvent(AccountEvent.OnLogout) },
            onSortRecipes = { viewModel.onEvent(AccountEvent.OnSortRecipes(it)) },
            onEditButtonClicked = { viewModel.onEvent(AccountEvent.OnEditButtonClicked) },
            onNameChange = { viewModel.onEvent(AccountEvent.EnteredName(it)) },
            onPasswordChange = { viewModel.onEvent(AccountEvent.EnteredPassword(it)) },
            onConfirmPasswordChange = { viewModel.onEvent(AccountEvent.EnteredConfirmPassword(it)) },
            onDialogDismiss = { viewModel.onEvent(AccountEvent.OnDismiss) },
            onDialogSave = { viewModel.onEvent(AccountEvent.OnSave) }
        )
    }
    else {
        UserNotLoggedInContent(
            onLogin = { viewModel.onEvent(AccountEvent.OnLogin) },
            onSignup = { viewModel.onEvent(AccountEvent.OnSignup) }
        )
    }
}