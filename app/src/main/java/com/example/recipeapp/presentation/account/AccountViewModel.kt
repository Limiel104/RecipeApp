package com.example.recipeapp.presentation.account

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _accountState = mutableStateOf(AccountState())
    val accountState: State<AccountState> = _accountState

    private val _accountUiEventChannel = Channel<AccountUiEvent>()
    val accountUiEventChannelFlow = _accountUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "Account ViewModel")
        checkIfUserLoggedIn()
    }

    fun onEvent(event: AccountEvent) {
        when(event) {
            AccountEvent.OnLogin -> {
                viewModelScope.launch {
                    _accountUiEventChannel.send(AccountUiEvent.NavigateToLogin)
                }
            }
            AccountEvent.OnSignup -> {
                viewModelScope.launch {
                    _accountUiEventChannel.send(AccountUiEvent.NavigateToSignup)
                }
            }
            is AccountEvent.OnLogout -> {
                viewModelScope.launch {
                    logout()
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _accountState.value = accountState.value.copy(
                isUserLoggedIn = currentUser != null
            )
        }
    }

    private fun logout() {
        logoutUseCase()
        _accountState.value = accountState.value.copy(
            isUserLoggedIn = false
        )
    }
}