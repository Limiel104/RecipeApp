package com.example.recipeapp.presentation.signup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.use_case.AddUserUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.SignupUseCase
import com.example.recipeapp.domain.use_case.ValidateConfirmPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.domain.use_case.ValidateSignupPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signupUseCase: SignupUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateSignupPasswordUseCase: ValidateSignupPasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val addUserUseCase: AddUserUseCase
): ViewModel() {

    private val _signupState = mutableStateOf(SignupState())
    val signupState: State<SignupState> = _signupState

    private val _signupUiEventChannel = Channel<SignupUiEvent>()
    val signupUiEventChannelFlow = _signupUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG","Signup ViewModel")

        savedStateHandle.get<String>("lastDestination")?.let { lastDestination ->
            _signupState.value = signupState.value.copy(
                lastDestination = lastDestination
            )
        }
    }

    fun onEvent(event: SignupEvent) {
        when(event) {
            is SignupEvent.EnteredEmail -> {
                _signupState.value = signupState.value.copy(
                    email = event.email
                )
            }
            is SignupEvent.EnteredPassword -> {
                _signupState.value = signupState.value.copy(
                    password = event.password
                )
            }
            is SignupEvent.EnteredConfirmPassword -> {
                _signupState.value = signupState.value.copy(
                    confirmPassword = event.confirmPassword
                )
            }
            is SignupEvent.EnteredName -> {
                _signupState.value = signupState.value.copy(
                    name = event.name
                )
            }
            SignupEvent.OnSignup -> {
                val email = _signupState.value.email
                val password = _signupState.value.password
                val confirmPassword = _signupState.value.confirmPassword
                val name = _signupState.value.name

                if(isValidationSuccessful(email, password, confirmPassword, name))
                    signup(email, password)
                else
                    Log.i("TAG", "Form validation error")
            }
            SignupEvent.OnGoBack -> {
                viewModelScope.launch {
                    _signupUiEventChannel.send(SignupUiEvent.NavigateBack)
                }
            }
        }
    }

    private fun isValidationSuccessful(
        email: String,
        password: String,
        confirmPassword: String,
        name: String,
    ): Boolean {
        val emailValidationResult = validateEmailUseCase(email)
        val passwordValidationResult = validateSignupPasswordUseCase(password)
        val confirmPasswordValidationResult = validateConfirmPasswordUseCase(password, confirmPassword)
        val nameValidationResult = validateNameUseCase(name)

        val hasError = listOf(
            emailValidationResult,
            passwordValidationResult,
            confirmPasswordValidationResult,
            nameValidationResult
        ).any { !it.isSuccessful }

        if(hasError) {
            _signupState.value = signupState.value.copy(
                emailError = emailValidationResult.errorMessage,
                passwordError = passwordValidationResult.errorMessage,
                confirmPasswordError = confirmPasswordValidationResult.errorMessage,
                nameError = nameValidationResult.errorMessage,
            )
            return false
        }

        _signupState.value = signupState.value.copy(
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            nameError = null,
        )
        return true
    }

    private fun signup(email: String, password: String) {
        viewModelScope.launch {
            signupUseCase(email,password).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.i("TAG","Error message from signup: $message")
                            _signupUiEventChannel.send(SignupUiEvent.ShowErrorMessage(message))
                        }
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading = ${response.isLoading}")
                        _signupState.value = signupState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG","Signup was successful")

                        val user = User(
                            userUID = getCurrentUserUseCase()!!.uid,
                            name = _signupState.value.name
                        )

                        addUser(user)
                    }
                }
            }
        }
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            addUserUseCase(user).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.i("TAG","Error message from add user: $message")
                            _signupUiEventChannel.send(SignupUiEvent.ShowErrorMessage(message))
                        }
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading = ${response.isLoading}")
                        _signupState.value = signupState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG","Added user successfully")
                        _signupUiEventChannel.send(SignupUiEvent.Signup)
                    }
                }
            }
        }
    }
}