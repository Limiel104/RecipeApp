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
import com.example.recipeapp.domain.use_case.ValidateConfirmPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.domain.use_case.ValidateSignupPasswordUseCase
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
    private val validateSignupPasswordUseCase: ValidateSignupPasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
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

            is AccountEvent.OnRecipeSelected -> {
                viewModelScope.launch { _accountUiEventChannel.send(AccountUiEvent.NavigateToRecipeDetails(event.recipeId)) }
            }

            AccountEvent.OnLogin -> {
                viewModelScope.launch { _accountUiEventChannel.send(AccountUiEvent.NavigateToLogin) }
            }

            AccountEvent.OnSignup -> {
                viewModelScope.launch { _accountUiEventChannel.send(AccountUiEvent.NavigateToSignup) }
            }

            AccountEvent.OnLogout -> {
                viewModelScope.launch { logout() }
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

                    if(isNameValidationSuccessful(_accountState.value.editName))
                        updateUser(user)
                    else
                        Log.i("TAG","Name validation not successful")
                }

                if(_accountState.value.password.isNotEmpty()) {
                    val password = _accountState.value.password
                    val confirmPassword = _accountState.value.confirmPassword

                    if(isPasswordValidationSuccessful(password, confirmPassword))
                        updateUserPassword(_accountState.value.password)
                    else
                        Log.i("TAG","Password validation not successful")
                }
            }

            AccountEvent.OnAddRecipeButtonClicked -> {
                viewModelScope.launch { _accountUiEventChannel.send(AccountUiEvent.NavigateToAddRecipe) }
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

    private fun isPasswordValidationSuccessful(
        password: String,
        confirmPassword: String
    ): Boolean {
        val passwordValidationResult = validateSignupPasswordUseCase(password)
        val confirmPasswordValidationResult = validateConfirmPasswordUseCase(password, confirmPassword)

        val hasError = listOf(
            passwordValidationResult,
            confirmPasswordValidationResult
        ).any { !it.isSuccessful }

        if(hasError) {
            _accountState.value = accountState.value.copy(
                passwordError = passwordValidationResult.errorMessage,
                confirmPasswordError = confirmPasswordValidationResult.errorMessage,
            )
            return false
        }

        _accountState.value = accountState.value.copy(
            passwordError = null,
            confirmPasswordError = null
        )
        return true
    }

    private fun isNameValidationSuccessful(name: String): Boolean {
        val nameValidationResult = validateNameUseCase(name)
        val hasError = listOf( nameValidationResult ).any { !it.isSuccessful }

        if(hasError) {
            _accountState.value = accountState.value.copy(
                nameError = nameValidationResult.errorMessage
            )
            return false
        }

        _accountState.value = accountState.value.copy(
            nameError = null
        )
        return true
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
                        _accountState.value = accountState.value.copy(
                            isEditDialogActivated = false,
                            editName = "",
                            password = "",
                            confirmPassword = "",
                        )
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
                            response.message?.let {
                                _accountUiEventChannel.send(AccountUiEvent.ShowErrorMessage(response.message))
                                Log.i("TAG","Error message from updateUserPassword: ${response.message}")
                            }
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
                        _accountState.value = accountState.value.copy(
                            isEditDialogActivated = false,
                            editName = "",
                            password = "",
                            confirmPassword = "",
                        )
                    }
                }
            }
        }
    }
}