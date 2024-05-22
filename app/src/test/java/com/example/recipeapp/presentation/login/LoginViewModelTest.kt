package com.example.recipeapp.presentation.login

import androidx.lifecycle.SavedStateHandle
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.LoginUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateLoginPasswordUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validateLoginPasswordUseCase: ValidateLoginPasswordUseCase
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        savedStateHandle = mockk()
        loginUseCase = mockk()
        validateEmailUseCase = ValidateEmailUseCase()
        validateLoginPasswordUseCase = ValidateLoginPasswordUseCase()
        firebaseUser = mockk()

        every { savedStateHandle.get<String>(any()) } returns "last_destination"
    }

    @After
    fun tearDown() {
        confirmVerified(savedStateHandle)
        confirmVerified(loginUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): LoginViewModel {
        return LoginViewModel(
            savedStateHandle,
            loginUseCase,
            validateEmailUseCase,
            validateLoginPasswordUseCase
        )
    }

    private fun getCurrentLoginState(): LoginState {
        return loginViewModel.loginState.value
    }

    @Test
    fun `saved state handle is set on init`() {
        loginViewModel = setViewModel()
        val result = getCurrentLoginState().lastDestination
        val isLoading = getCurrentLoginState().isLoading

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(result).isEqualTo("last_destination")
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `enteredEmail - initially empty`() {
        loginViewModel = setViewModel()
        val initialEmailState = getCurrentLoginState().email

        loginViewModel.onEvent(LoginEvent.EnteredEmail("email"))
        val resultEmailState = getCurrentLoginState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEmpty()
        assertThat(resultEmailState).isEqualTo("email")
    }

    @Test
    fun `enteredEmail - initially not empty - changed string`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredEmail("email"))
        val initialEmailState = getCurrentLoginState().email

        loginViewModel.onEvent(LoginEvent.EnteredEmail("john@email.com"))
        val resultEmailState = getCurrentLoginState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEqualTo("email")
        assertThat(resultEmailState).isEqualTo("john@email.com")
    }

    @Test
    fun `enteredEmail - initially not empty - result empty`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredEmail("email"))
        val initialEmailState = getCurrentLoginState().email

        loginViewModel.onEvent(LoginEvent.EnteredEmail(""))
        val resultEmailState = getCurrentLoginState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEqualTo("email")
        assertThat(resultEmailState).isEmpty()
    }

    @Test
    fun `enteredPassword - initially empty`() {
        loginViewModel = setViewModel()
        val initialPasswordState = getCurrentLoginState().password

        loginViewModel.onEvent(LoginEvent.EnteredPassword("password"))
        val resultPasswordState = getCurrentLoginState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEmpty()
        assertThat(resultPasswordState).isEqualTo("password")
    }

    @Test
    fun `enteredPassword - initially not empty - changed string`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentLoginState().password

        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        val resultPasswordState = getCurrentLoginState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEqualTo("Qwerty1+")
    }

    @Test
    fun `enteredPassword - initially not empty - result empty`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentLoginState().password

        loginViewModel.onEvent(LoginEvent.EnteredPassword(""))
        val resultPasswordState = getCurrentLoginState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEmpty()
    }

    @Test
    fun `onLogin - email is empty`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        val initialEmailErrorState = getCurrentLoginState().emailError

        loginViewModel.onEvent(LoginEvent.OnLogin)
        val resultEmailErrorState = getCurrentLoginState().emailError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailErrorState).isNull()
        assertThat(resultEmailErrorState).isEqualTo("Email can't be empty")
    }

    @Test
    fun `onLogin - email in wrong format`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        val initialEmailErrorState = getCurrentLoginState().emailError

        loginViewModel.onEvent(LoginEvent.EnteredEmail("john#smith@email.com"))
        loginViewModel.onEvent(LoginEvent.OnLogin)
        val resultEmailErrorState = getCurrentLoginState().emailError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailErrorState).isNull()
        assertThat(resultEmailErrorState).isEqualTo("Email in wrong format")
    }

    @Test
    fun `onLogin - password is empty`() {
        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredEmail("john.smith@email.com"))
        val initialPasswordErrorState = getCurrentLoginState().passwordError

        loginViewModel.onEvent(LoginEvent.OnLogin)
        val resultPasswordErrorState = getCurrentLoginState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password can't be empty")
    }

    @Test
    fun `onLogin - email and password are correct`() {
        val result = Resource.Success(firebaseUser)

        coEvery { loginUseCase(any(), any()) } returns flowOf(result)

        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredEmail("john.smith@email.com"))
        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        val initialEmailErrorState = getCurrentLoginState().emailError
        val initialPasswordErrorState = getCurrentLoginState().passwordError

        loginViewModel.onEvent(LoginEvent.OnLogin)
        val resultEmailErrorState = getCurrentLoginState().emailError
        val resultPasswordErrorState = getCurrentLoginState().passwordError

        coVerifySequence {
            savedStateHandle.get<String>("lastDestination")
            loginUseCase("john.smith@email.com","Qwerty1+")
        }
        assertThat(initialEmailErrorState).isNull()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultEmailErrorState).isNull()
        assertThat(resultPasswordErrorState).isNull()
    }

    @Test
    fun `onLogin - email and password are passed correctly`() {
        val result = Resource.Success(firebaseUser)
        val email = slot<String>()
        val password = slot<String>()

        coEvery { loginUseCase(capture(email), capture(password)) } returns flowOf(result)
        excludeRecords { savedStateHandle.get<String>("lastDestination") }

        loginViewModel = setViewModel()
        loginViewModel.onEvent(LoginEvent.EnteredEmail("john.smith@email.com"))
        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        loginViewModel.onEvent(LoginEvent.OnLogin)

        coVerify(exactly = 1) { loginUseCase(any(), any()) }
        assertThat(email.captured).isEqualTo("john.smith@email.com")
        assertThat(password.captured).isEqualTo("Qwerty1+")
    }

    @Test
    fun `onLogin - is loading`() {
        coEvery { loginUseCase(any(), any()) } returns flowOf(Resource.Loading(true))

        loginViewModel = setViewModel()
        val initialLoadingState = getCurrentLoginState().isLoading

        loginViewModel.onEvent(LoginEvent.EnteredEmail("john.smith@email.com"))
        loginViewModel.onEvent(LoginEvent.EnteredPassword("Qwerty1+"))
        loginViewModel.onEvent(LoginEvent.OnLogin)
        val  resultLoadingState = getCurrentLoginState().isLoading

        coVerify(exactly = 1) {
            savedStateHandle.get<String>("lastDestination")
            loginUseCase("john.smith@email.com", "Qwerty1+")
        }
        assertThat(initialLoadingState).isFalse()
        assertThat(resultLoadingState).isTrue()
    }
}