package com.example.recipeapp.presentation.shopping_list.composable

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.presentation.common.composable.UserNotLoggedInContent
import com.example.recipeapp.presentation.navigation.Screen
import com.example.recipeapp.presentation.shopping_list.ShoppingListEvent
import com.example.recipeapp.presentation.shopping_list.ShoppingListUiEvent
import com.example.recipeapp.presentation.shopping_list.ShoppingListViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isUserLoggedIn = viewModel.shoppingListState.value.isUserLoggedIn
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.shoppingListUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "ShoppingList Screen LE")
                when (event) {
                    ShoppingListUiEvent.NavigateToLogin -> {
                        navController.navigate(Screen.LoginScreen.route)
                    }
                    ShoppingListUiEvent.NavigateToSignup -> {
                        navController.navigate(Screen.SignupScreen.route)
                    }
                }
            }
        }
    }
    if(isUserLoggedIn) {
        ShoppingListContent(
            scrollBehavior = scrollBehavior
        )
    }
    else {
        UserNotLoggedInContent(
            onLogin = { viewModel.onEvent(ShoppingListEvent.OnLogin) },
            onSignup = { viewModel.onEvent(ShoppingListEvent.OnSignup) }
        )
    }
}