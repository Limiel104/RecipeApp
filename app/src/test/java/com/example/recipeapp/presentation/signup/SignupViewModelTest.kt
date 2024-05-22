package com.example.recipeapp.presentation.signup

import androidx.lifecycle.SavedStateHandle
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.model.User
import com.example.recipeapp.domain.use_case.AddUserUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.SignupUseCase
import com.example.recipeapp.domain.use_case.ValidateConfirmPasswordUseCase
import com.example.recipeapp.domain.use_case.ValidateEmailUseCase
import com.example.recipeapp.domain.use_case.ValidateNameUseCase
import com.example.recipeapp.domain.use_case.ValidateSignupPasswordUseCase
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

class SignupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var signupUseCase: SignupUseCase
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validateSignupPasswordUseCase: ValidateSignupPasswordUseCase
    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var user: User

    @Before
    fun setUp() {
        savedStateHandle = mockk()
        signupUseCase = mockk()
        validateEmailUseCase = ValidateEmailUseCase()
        validateSignupPasswordUseCase = ValidateSignupPasswordUseCase()
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()
        validateNameUseCase = ValidateNameUseCase()
        getCurrentUserUseCase = mockk()
        addUserUseCase = mockk()
        firebaseUser = mockk()

        user = User(
            userUID = "userUID",
            name = "JohnSmith4"
        )

        every { savedStateHandle.get<String>(any()) } returns "last_destination"
        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"
    }

    @After
    fun tearDown() {
        confirmVerified(savedStateHandle)
        confirmVerified(signupUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(addUserUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): SignupViewModel {
        return SignupViewModel(
            savedStateHandle,
            signupUseCase,
            validateEmailUseCase,
            validateSignupPasswordUseCase,
            validateConfirmPasswordUseCase,
            validateNameUseCase,
            getCurrentUserUseCase,
            addUserUseCase
        )
    }

    private fun getCurrentSignupState(): SignupState {
        return signupViewModel.signupState.value
    }

    @Test
    fun `saved state handle is set on init`() {
        signupViewModel = setViewModel()
        val result = getCurrentSignupState().lastDestination
        val isLoading = getCurrentSignupState().isLoading

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(result).isEqualTo("last_destination")
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `enteredEmail - initially empty`() {
        signupViewModel = setViewModel()
        val initialEmailState = getCurrentSignupState().email

        signupViewModel.onEvent(SignupEvent.EnteredEmail("email"))
        val resultEmailState = getCurrentSignupState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEmpty()
        assertThat(resultEmailState).isEqualTo("email")
    }

    @Test
    fun `enteredEmail - initially not empty - changed string`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("email"))
        val initialEmailState = getCurrentSignupState().email

        signupViewModel.onEvent(SignupEvent.EnteredEmail("john@email.com"))
        val resultEmailState = getCurrentSignupState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEqualTo("email")
        assertThat(resultEmailState).isEqualTo("john@email.com")
    }

    @Test
    fun `enteredEmail - initially not empty - result empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("email"))
        val initialEmailState = getCurrentSignupState().email

        signupViewModel.onEvent(SignupEvent.EnteredEmail(""))
        val resultEmailState = getCurrentSignupState().email

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailState).isEqualTo("email")
        assertThat(resultEmailState).isEmpty()
    }

    @Test
    fun `enteredPassword - initially empty`() {
        signupViewModel = setViewModel()
        val initialPasswordState = getCurrentSignupState().password

        signupViewModel.onEvent(SignupEvent.EnteredPassword("password"))
        val resultPasswordState = getCurrentSignupState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEmpty()
        assertThat(resultPasswordState).isEqualTo("password")
    }

    @Test
    fun `enteredPassword - initially not empty - changed string`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentSignupState().password

        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        val resultPasswordState = getCurrentSignupState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEqualTo("Qwerty1+")
    }

    @Test
    fun `enteredPassword - initially not empty - result empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentSignupState().password

        signupViewModel.onEvent(SignupEvent.EnteredPassword(""))
        val resultPasswordState = getCurrentSignupState().password

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEmpty()
    }

    @Test
    fun `enteredConfirmPassword - initially empty`() {
        signupViewModel = setViewModel()
        val initialConfirmPasswordState = getCurrentSignupState().confirmPassword

        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("password"))
        val resultConfirmPasswordState = getCurrentSignupState().confirmPassword

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialConfirmPasswordState).isEmpty()
        assertThat(resultConfirmPasswordState).isEqualTo("password")
    }

    @Test
    fun `enteredConfirmPassword - initially not empty - changed string`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("password"))
        val initialConfirmPasswordState = getCurrentSignupState().confirmPassword

        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        val resultConfirmPasswordState = getCurrentSignupState().confirmPassword

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialConfirmPasswordState).isEqualTo("password")
        assertThat(resultConfirmPasswordState).isEqualTo("Qwerty1+")
    }

    @Test
    fun `enteredConfirmPassword - initially not empty - result empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("password"))
        val initialConfirmPasswordState = getCurrentSignupState().confirmPassword

        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword(""))
        val resultConfirmPasswordState = getCurrentSignupState().confirmPassword

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialConfirmPasswordState).isEqualTo("password")
        assertThat(resultConfirmPasswordState).isEmpty()
    }

    @Test
    fun `enteredName - initially empty`() {
        signupViewModel = setViewModel()
        val initialNameState = getCurrentSignupState().name

        signupViewModel.onEvent(SignupEvent.EnteredName("name"))
        val resultNameState = getCurrentSignupState().name

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameState).isEmpty()
        assertThat(resultNameState).isEqualTo("name")
    }

    @Test
    fun `enteredName - initially not empty - changed string`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredName("name"))
        val initialNameState = getCurrentSignupState().name

        signupViewModel.onEvent(SignupEvent.EnteredName("John4"))
        val resultNameState = getCurrentSignupState().name

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameState).isEqualTo("name")
        assertThat(resultNameState).isEqualTo("John4")
    }

    @Test
    fun `enteredName - initially not empty - result empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredName("name"))
        val initialNameState = getCurrentSignupState().name

        signupViewModel.onEvent(SignupEvent.EnteredName(""))
        val resultNameState = getCurrentSignupState().name

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameState).isEqualTo("name")
        assertThat(resultNameState).isEmpty()
    }

    @Test
    fun `onSignup - email is empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialEmailErrorState = getCurrentSignupState().emailError

        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultEmailErrorState = getCurrentSignupState().emailError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailErrorState).isNull()
        assertThat(resultEmailErrorState).isEqualTo("Email can't be empty")
    }

    @Test
    fun `onSignup - email in wrong format`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialEmailErrorState = getCurrentSignupState().emailError

        signupViewModel.onEvent(SignupEvent.EnteredEmail("john#smith@email.com"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultEmailErrorState = getCurrentSignupState().emailError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialEmailErrorState).isNull()
        assertThat(resultEmailErrorState).isEqualTo("Email in wrong format")
    }

    @Test
    fun `onSignup - password is empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword(""))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialPasswordErrorState = getCurrentSignupState().passwordError

        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultPasswordErrorState = getCurrentSignupState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password can't be empty")
    }

    @Test
    fun `onSignup - password is too short`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwe"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialPasswordErrorState = getCurrentSignupState().passwordError

        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwe"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultPasswordErrorState = getCurrentSignupState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password is too short")
    }

    @Test
    fun `onSignup - password does not have at least one digit`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty++"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialPasswordErrorState = getCurrentSignupState().passwordError

        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty++"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultPasswordErrorState = getCurrentSignupState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one digit")
    }

    @Test
    fun `onSignup - password does not have at least one capital letter`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialPasswordErrorState = getCurrentSignupState().passwordError

        signupViewModel.onEvent(SignupEvent.EnteredPassword("qwerty1+"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultPasswordErrorState = getCurrentSignupState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one capital letter")
    }

    @Test
    fun `onSignup - password does not have at least one special character`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty11"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialPasswordErrorState = getCurrentSignupState().passwordError

        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty11"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultPasswordErrorState = getCurrentSignupState().passwordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one special character")
    }

    @Test
    fun `onSignup - confirm password does not match`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty11"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialConfirmPasswordErrorState = getCurrentSignupState().confirmPasswordError

        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultConfirmPasswordErrorState = getCurrentSignupState().confirmPasswordError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialConfirmPasswordErrorState).isNull()
        assertThat(resultConfirmPasswordErrorState).isEqualTo("Passwords don't mach")
    }

    @Test
    fun `onSignup - name is empty`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        val initialNameErrorState = getCurrentSignupState().nameError

        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultNameErrorState = getCurrentSignupState().nameError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameErrorState).isNull()
        assertThat(resultNameErrorState).isEqualTo("Name can't be empty")
    }

    @Test
    fun `onSignup - name is too short`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        val initialNameErrorState = getCurrentSignupState().nameError

        signupViewModel.onEvent(SignupEvent.EnteredName("John4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultNameErrorState = getCurrentSignupState().nameError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameErrorState).isNull()
        assertThat(resultNameErrorState).isEqualTo("Name is too short")
    }

    @Test
    fun `onSignup - name has at least one not allowed character`() {
        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        val initialNameErrorState = getCurrentSignupState().nameError

        signupViewModel.onEvent(SignupEvent.EnteredName("John@Smith4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultNameErrorState = getCurrentSignupState().nameError

        verify(exactly = 1) { savedStateHandle.get<String>("lastDestination") }
        assertThat(initialNameErrorState).isNull()
        assertThat(resultNameErrorState).isEqualTo("At least one character in name is not allowed")
    }

    @Test
    fun `onSignup - all fields are correct`() {
        val result = Resource.Success(firebaseUser)
        val result2 = Resource.Success(true)

        coEvery { signupUseCase(any(), any()) } returns flowOf(result)
        coEvery { addUserUseCase(any()) } returns flowOf(result2)

        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        val initialEmailErrorState = getCurrentSignupState().emailError
        val initialPasswordErrorState = getCurrentSignupState().passwordError
        val initialConfirmPasswordErrorState = getCurrentSignupState().passwordError
        val initialNameErrorState = getCurrentSignupState().nameError

        signupViewModel.onEvent(SignupEvent.OnSignup)
        val resultEmailErrorState = getCurrentSignupState().emailError
        val resultPasswordErrorState = getCurrentSignupState().passwordError
        val resultConfirmPasswordErrorState = getCurrentSignupState().passwordError
        val resultNameErrorState = getCurrentSignupState().nameError

        coVerifySequence {
            savedStateHandle.get<String>("lastDestination")
            signupUseCase("john.smith@email.com","Qwerty1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(user)
        }
        assertThat(initialEmailErrorState).isNull()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(initialConfirmPasswordErrorState).isNull()
        assertThat(initialNameErrorState).isNull()
        assertThat(resultEmailErrorState).isNull()
        assertThat(resultPasswordErrorState).isNull()
        assertThat(resultConfirmPasswordErrorState).isNull()
        assertThat(resultNameErrorState).isNull()
    }

    @Test
    fun `onSignup - email and password are passed correctly`() {
        val result = Resource.Success(firebaseUser)
        val result2 = Resource.Success(true)
        val email = slot<String>()
        val password = slot<String>()

        coEvery { signupUseCase(capture(email), capture(password)) } returns flowOf(result)
        excludeRecords { savedStateHandle.get<String>("lastDestination") }
        coEvery { addUserUseCase(any()) } returns flowOf(result2)

        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)

        coVerifySequence {
            signupUseCase(any(), any())
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(user)
        }
        assertThat(email.captured).isEqualTo("john.smith@email.com")
        assertThat(password.captured).isEqualTo("Qwerty1+")
    }

    @Test
    fun `onSignup - user is passed correctly`() {
        val result = Resource.Success(firebaseUser)
        val result2 = Resource.Success(true)
        val capturedUser = slot<User>()

        coEvery { signupUseCase(any(), any()) } returns flowOf(result)
        excludeRecords { savedStateHandle.get<String>("lastDestination") }
        coEvery { addUserUseCase(capture(capturedUser)) } returns flowOf(result2)

        signupViewModel = setViewModel()
        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)

        coVerifySequence {
            signupUseCase(any(), any())
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(user)
        }
        assertThat(capturedUser.captured.userUID).isEqualTo("userUID")
        assertThat(capturedUser.captured.name).isEqualTo("JohnSmith4")
    }

    @Test
    fun `onSignup - is loading`() {
        coEvery { signupUseCase(any(), any()) } returns flowOf(Resource.Loading(true))

        signupViewModel = setViewModel()
        val initialLoadingState = getCurrentSignupState().isLoading

        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val  resultLoadingState = getCurrentSignupState().isLoading

        coVerify(exactly = 1) {
            savedStateHandle.get<String>("lastDestination")
            signupUseCase("john.smith@email.com", "Qwerty1+")
        }
        assertThat(initialLoadingState).isFalse()
        assertThat(resultLoadingState).isTrue()
    }

    @Test
    fun `addUser - is loading`() {
        val result = Resource.Success(firebaseUser)

        coEvery { signupUseCase(any(), any()) } returns flowOf(result)
        coEvery { addUserUseCase(any()) } returns flowOf(Resource.Loading(true))

        signupViewModel = setViewModel()
        val initialLoadingState = getCurrentSignupState().isLoading

        signupViewModel.onEvent(SignupEvent.EnteredEmail("john.smith@email.com"))
        signupViewModel.onEvent(SignupEvent.EnteredPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredConfirmPassword("Qwerty1+"))
        signupViewModel.onEvent(SignupEvent.EnteredName("JohnSmith4"))
        signupViewModel.onEvent(SignupEvent.OnSignup)
        val  resultLoadingState = getCurrentSignupState().isLoading

        coVerify(exactly = 1) {
            savedStateHandle.get<String>("lastDestination")
            signupUseCase("john.smith@email.com", "Qwerty1+")
            getCurrentUserUseCase()
            firebaseUser.uid
            addUserUseCase(user)
        }
        assertThat(initialLoadingState).isFalse()
        assertThat(resultLoadingState).isTrue()
    }
}