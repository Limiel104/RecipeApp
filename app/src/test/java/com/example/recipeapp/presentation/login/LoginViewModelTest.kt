package com.example.recipeapp.presentation.login

import androidx.lifecycle.SavedStateHandle
import com.example.recipeapp.domain.use_case.LoginUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateLoginPasswordUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    @Before
    fun setUp() {
        savedStateHandle = mockk()
        loginUseCase = mockk()
        validateEmailUseCase = ValidateEmailUseCase()
        validateLoginPasswordUseCase = ValidateLoginPasswordUseCase()

        every { savedStateHandle.get<String>(any()) } returns "last_destination"
    }

    @After
    fun tearDown() {
        confirmVerified(savedStateHandle)
        confirmVerified(loginUseCase)
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
}