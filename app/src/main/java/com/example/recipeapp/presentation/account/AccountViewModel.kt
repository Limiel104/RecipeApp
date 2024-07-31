package com.example.recipeapp.presentation.account

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetUserRecipesUseCase
import com.example.recipeapp.domain.use_case.GetUserUseCase
import com.example.recipeapp.domain.use_case.LogoutUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.use_case.UpdateUserPasswordUseCase
import com.example.recipeapp.domain.use_case.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserRecipesUseCase: GetUserRecipesUseCase,
    private val sortRecipesUseCase: SortRecipesUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
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
            is AccountEvent.OnSortRecipes -> {
                _accountState.value = accountState.value.copy(
                    recipesOrder = event.recipeOrder
                )

                _accountState.value = accountState.value.copy(
                    recipes = sortRecipesUseCase(event.recipeOrder, _accountState.value.recipes)
                )
            }

            is AccountEvent.EnteredName -> {
                _accountState.value = accountState.value.copy(
                    editName = event.name
                )
            }

            is AccountEvent.EnteredPassword -> {
                _accountState.value = accountState.value.copy(
                    password = event.password
                )
            }

            is AccountEvent.EnteredConfirmPassword -> {
                _accountState.value = accountState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }

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

            AccountEvent.OnLogout -> {
                viewModelScope.launch {
                    logout()
                }
            }

            AccountEvent.OnEditButtonClicked -> {
                _accountState.value = accountState.value.copy(
                    isEditDialogActivated = true
                )
            }

            AccountEvent.OnDismiss -> {
                _accountState.value = accountState.value.copy(
                    isEditDialogActivated = false,
                    editName = "",
                    password = "",
                    confirmPassword = "",
                )
            }

            AccountEvent.OnSave -> {
                if(_accountState.value.editName.isNotEmpty()) {
                    val user = User(
                        userUID = _accountState.value.userUID,
                        name = _accountState.value.editName
                    )
                    Log.i("TAG",user.toString())
                    updateUser(user)
                }

                if(_accountState.value.password.isNotEmpty()) {
                    updateUserPassword(_accountState.value.password)
                }

                _accountState.value = accountState.value.copy(
                    isEditDialogActivated = false,
                    editName = "",
                    password = "",
                    confirmPassword = "",
                )
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _accountState.value = accountState.value.copy(
                isUserLoggedIn = currentUser != null
            )

            currentUser?.let {
                getUser(currentUser.uid)
                getUserRecipes(currentUser.uid)
            }
        }
    }

    private fun logout() {
        logoutUseCase()
        _accountState.value = accountState.value.copy(
            isUserLoggedIn = false
        )
    }

    private fun getUser(userUID: String) {
        viewModelScope.launch {
            getUserUseCase(userUID).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getUser: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading user: ${response.isLoading}")
                        _accountState.value = accountState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _accountState.value = accountState.value.copy(
                                userUID = response.data.userUID,
                                name = response.data.name
                            )
                            Log.i("TAG","user: ${response.data}")
                        }
                    }
                }

            }
        }
    }

    private fun getUserRecipes(userUID: String) {
        viewModelScope.launch {
            getUserRecipesUseCase(userUID).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getUserRecipes: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading user recipes: ${response.isLoading}")
                        _accountState.value = accountState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _accountState.value = accountState.value.copy(
                                recipes = sortRecipesUseCase(_accountState.value.recipesOrder,response.data)
                            )
                            Log.i("TAG","user recipes: ${response.data}")
                        }
                    }
                }
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            updateUserUseCase(user).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from updateUser: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading update user: ${response.isLoading}")
                        _accountState.value = accountState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let { Log.i("TAG","update user: ${response.data}") }
                    }
                }
            }
        }
    }

    private fun updateUserPassword(password: String) {
        viewModelScope.launch {
            updateUserPasswordUseCase(password).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        viewModelScope.launch {
                            _accountUiEventChannel.send(AccountUiEvent.NavigateToSignup)
                            Log.i("TAG","Error message from updateUserPassword: ${response.message}")
                        }
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading updateUserPassword: ${response.isLoading}")
                        _accountState.value = accountState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let { Log.i("TAG","updateUserPassword: ${response.data}") }
                    }
                }
            }
        }
    }
}