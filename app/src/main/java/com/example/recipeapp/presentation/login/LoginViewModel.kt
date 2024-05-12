package com.example.recipeapp.presentation.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.LoginUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateLoginPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateLoginPasswordUseCase: ValidateLoginPasswordUseCase
): ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _loginUiEventChannel = Channel<LoginUiEvent>()
    val loginUiEventChannelFlow = _loginUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG","Login ViewModel")

        savedStateHandle.get<String>("lastDestination")?.let { lastDestination ->
            _loginState.value = loginState.value.copy(
                lastDestination = lastDestination
            )
        }
    }

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EnteredEmail -> {
                _loginState.value = loginState.value.copy(
                    email = event.email
                )
            }
            is LoginEvent.EnteredPassword -> {
                _loginState.value = loginState.value.copy(
                    password = event.password
                )
            }
            LoginEvent.OnLogin -> {
                val email = _loginState.value.email
                val password = _loginState.value.password

                if(isValidationSuccessful(email, password))
                    login(email, password)
                else
                    Log.i("TAG", "Form validation error")
            }
            LoginEvent.OnSignup -> {
                viewModelScope.launch {
                    _loginUiEventChannel.send(LoginUiEvent.NavigateToSignup)
                }
            }
            LoginEvent.OnGoBack -> {
                viewModelScope.launch {
                    _loginUiEventChannel.send(LoginUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun isValidationSuccessful(email: String, password: String): Boolean {
        val emailValidationResult = validateEmailUseCase(email)
        val passwordValidationResult = validateLoginPasswordUseCase(password)

        val hasError = listOf(
            emailValidationResult,
            passwordValidationResult
        ).any { !it.isSuccessful }

        if (hasError) {
            _loginState.value = loginState.value.copy(
                emailError = emailValidationResult.errorMessage,
                passwordError = passwordValidationResult.errorMessage
            )
            return false
        }

        _loginState.value = loginState.value.copy(
            emailError = null,
            passwordError = null
        )
        return true
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email,password).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.i("TAG","Error message from login: $message")
                            _loginUiEventChannel.send(LoginUiEvent.ShowErrorMessage(message))
                        }
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading = ${response.isLoading}")
                        _loginState.value = loginState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        _loginUiEventChannel.send(LoginUiEvent.Login)
                    }
                }
            }
        }
    }
}