package com.example.recipeapp.presentation.shopping_list.composable

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
    val modalBottomSheetState = rememberModalBottomSheetState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.shoppingListUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "ShoppingList Screen LE")
                when (event) {
                    ShoppingListUiEvent.NavigateToLogin -> {
                        navController.navigate(Screen.LoginScreen.route + "lastDestination=" + Screen.ShoppingListScreen.route)
                    }
                    ShoppingListUiEvent.NavigateToSignup -> {
                        navController.navigate(Screen.SignupScreen.route + "lastDestination=" + Screen.ShoppingListScreen.route)
                    }
                }
            }
        }
    }
    if(isUserLoggedIn) {
        ShoppingListContent(
            scrollBehavior = scrollBehavior,
            modalBottomSheetState = modalBottomSheetState,
            uiState = viewModel.shoppingListState.value,
            onIngredientSuggestionClick = { viewModel.onEvent(ShoppingListEvent.SelectedIngredient(it)) },
            onDropDownMenuExpandedChange = { viewModel.onEvent(ShoppingListEvent.OnDropDownMenuExpandChange) },
            onIngredientChange = { viewModel.onEvent(ShoppingListEvent.EnteredIngredient(it)) },
            onAddIngredientsDialogDismiss = { viewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogDismiss) },
            onAddIngredientsSave = { viewModel.onEvent(ShoppingListEvent.OnAddIngredientsDialogSave) },
            onAddButtonClick = { viewModel.onEvent(ShoppingListEvent.OnAddButtonClicked) },
            onIngredientClick = { viewModel.onEvent(ShoppingListEvent.OnIngredientClicked(it)) },
            onSelectedWholeQuantity = { viewModel.onEvent(ShoppingListEvent.SelectedWholeQuantity(it)) },
            onSelectedDecimalQuantity = { viewModel.onEvent(ShoppingListEvent.SelectedDecimalQuantity(it)) },
            onSelectedTypeQuantity = { viewModel.onEvent(ShoppingListEvent.SelectedTypeQuantity(it)) },
            onQuantityPickerDismiss = { viewModel.onEvent(ShoppingListEvent.OnQuantityPickerDismissed) },
            onQuantityPickerSave = { viewModel.onEvent(ShoppingListEvent.OnQuantityPickerSaved) },
            onCheckedChange = { viewModel.onEvent(ShoppingListEvent.OnCheckBoxToggled(it)) },
            onMenuButtonClicked = { viewModel.onEvent(ShoppingListEvent.OnMenuButtonClicked) },
            onMenuDismissed = { viewModel.onEvent(ShoppingListEvent.OnMenuDismissed) },
            onNameChange = { viewModel.onEvent(ShoppingListEvent.EnteredName(it)) },
            onOpenRenameShoppingListDialog = { viewModel.onEvent(ShoppingListEvent.OnOpenRenameShoppingListDialog) },
            onRenameShoppingListDialogSaved = { viewModel.onEvent(ShoppingListEvent.OnRenameShoppingListDialogSaved) },
            onRenameShoppingListDialogDismissed = { viewModel.onEvent(ShoppingListEvent.OnRenameShoppingListDialogDismissed) },
            onDeleteAllIngredients = { viewModel.onEvent(ShoppingListEvent.OnDeleteAllIngredients) },
            onDeleteShoppingList = { viewModel.onEvent(ShoppingListEvent.OnDeleteShoppingList) },
            onOpenOtherShoppingListsMenu = { viewModel.onEvent(ShoppingListEvent.OnOpenOtherShoppingListsMenu) },
            onOtherShoppingListsMenuDismiss = { viewModel.onEvent(ShoppingListEvent.OnOtherShoppingListsMenuDismissed) }
        )
    }
    else {
        UserNotLoggedInContent(
            onLogin = { viewModel.onEvent(ShoppingListEvent.OnLogin) },
            onSignup = { viewModel.onEvent(ShoppingListEvent.OnSignup) }
        )
    }
}