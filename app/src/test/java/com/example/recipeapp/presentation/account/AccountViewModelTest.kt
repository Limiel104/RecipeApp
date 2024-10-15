package com.example.recipeapp.presentation.account

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
import com.example.recipeapp.domain.util.RecipeOrder
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var updateUserPasswordUseCase: UpdateUserPasswordUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getUserRecipesUseCase: GetUserRecipesUseCase
    private lateinit var sortRecipesUseCase: SortRecipesUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var validateSignupPasswordUseCase: ValidateSignupPasswordUseCase
    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var user: User
    private lateinit var updatedUser: User

    @Before
    fun setUp() {
        sortRecipesUseCase = SortRecipesUseCase()
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()
        validateSignupPasswordUseCase = ValidateSignupPasswordUseCase()
        validateNameUseCase = ValidateNameUseCase()
        getUserUseCase = mockk()
        updateUserPasswordUseCase = mockk()
        updateUserUseCase = mockk()
        getUserRecipesUseCase = mockk()
        getCurrentUserUseCase = mockk()
        logoutUseCase = mockk()
        firebaseUser = mockk()

        user = User(
            userUID = "userUID",
            name = "User Name"
        )

        updatedUser = User(
            userUID = "userUID",
            name = "User Updated Name"
        )

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"
    }

    @After
    fun tearDown() {
        confirmVerified(getUserUseCase)
        confirmVerified(getUserRecipesUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(logoutUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): AccountViewModel {
        return AccountViewModel(
            updateUserPasswordUseCase,
            getCurrentUserUseCase,
            getUserRecipesUseCase,
            sortRecipesUseCase,
            updateUserUseCase,
            validateSignupPasswordUseCase,
            validateConfirmPasswordUseCase,
            validateNameUseCase,
            getUserUseCase,
            logoutUseCase)
    }

    private fun getCurrentAccountState(): AccountState {
        return accountViewModel.accountState.value
    }


    private fun verifyMocks() {
        coVerifyOrder {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
        }
    }

    private fun verifyAllMocks() {
        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
        }
    }

    private fun setMocks() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(getRecipes()))
    }

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        setMocks()
        accountViewModel = setViewModel()
        val result = getCurrentAccountState().isUserLoggedIn

        verifyAllMocks()
        assertThat(result).isTrue()
    }

    @Test
    fun `checkIfUserLoggedIn - user is not logged in`() {
        every { getCurrentUserUseCase() } returns null

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().isUserLoggedIn

        verifySequence { getCurrentUserUseCase() }
        assertThat(result).isFalse()
    }

    @Test
    fun `getUser sets user data successfully`() {
        setMocks()
        accountViewModel = setViewModel()
        val resultUserUID = getCurrentAccountState().userUID
        val resultUserName = getCurrentAccountState().name
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(resultUserUID).isEqualTo("userUID")
        assertThat(resultUserName).isEqualTo("User Name")
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUser returns error`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Error("Error message"))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        accountViewModel = setViewModel()
        val resultUserUID = getCurrentAccountState().userUID
        val resultUserName = getCurrentAccountState().name
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(resultUserUID).isEqualTo("")
        assertThat(resultUserName).isEqualTo("")
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUser is loading`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Loading(true))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(getRecipes()))

        accountViewModel = setViewModel()
        val resultUserUID = getCurrentAccountState().userUID
        val resultUserName = getCurrentAccountState().name
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(resultUserUID).isEqualTo("")
        assertThat(resultUserName).isEqualTo("")
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getUserRecipes sets recipes successfully`() {
        setMocks()
        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).containsExactlyElementsIn(getRecipes())
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserRecipes returns error`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserRecipes is loading`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Loading(true))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `updateUser sets user data successfully`() {
        setMocks()
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredName(updatedUser.name))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserUseCase(updatedUser)
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEmpty()
        assertThat(resultPassword).isEmpty()
        assertThat(resultConfirmPassword).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `updateUser returns error`() {
        setMocks()
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredName(updatedUser.name))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserUseCase(updatedUser)
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEqualTo(updatedUser.name)
        assertThat(resultPassword).isEmpty()
        assertThat(resultConfirmPassword).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `updateUser is loading`() {
        setMocks()
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Loading(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredName(updatedUser.name))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserUseCase(updatedUser)
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEqualTo(updatedUser.name)
        assertThat(resultPassword).isEmpty()
        assertThat(resultConfirmPassword).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `updateUserPassword sets recipes successfully`() {
        setMocks()
        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserPasswordUseCase("newPassword1+")
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEmpty()
        assertThat(resultPassword).isEmpty()
        assertThat(resultConfirmPassword).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `updateUserPassword returns error`() {
        setMocks()
        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserPasswordUseCase("newPassword1+")
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEmpty()
        assertThat(resultPassword).isEqualTo("newPassword1+")
        assertThat(resultConfirmPassword).isEqualTo("newPassword1+")
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `updateUserPassword is loading`() {
        setMocks()
        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Loading(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultIsEditDialogActivated = getCurrentAccountState().isEditDialogActivated
        val resultEditName = getCurrentAccountState().editName
        val resultPassword = getCurrentAccountState().password
        val resultConfirmPassword = getCurrentAccountState().confirmPassword
        val isLoading = getCurrentAccountState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserPasswordUseCase("newPassword1+")
        }
        assertThat(resultIsEditDialogActivated).isFalse()
        assertThat(resultEditName).isEmpty()
        assertThat(resultPassword).isEqualTo("newPassword1+")
        assertThat(resultConfirmPassword).isEqualTo("newPassword1+")
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getUserRecipes - recipes are sorted correctly`() {
        setMocks()
        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in descending order`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateDescending))
        val result = getCurrentAccountState().recipes

        verifyAllMocks()
        assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in ascending order`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateAscending))
        val result = getCurrentAccountState().recipes

        verifyAllMocks()
        assertThat(result).isEqualTo(getRecipes().sortedBy { it.date })
    }

    @Test
    fun `EnteredName - initially empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val initialNameState = getCurrentAccountState().editName

        accountViewModel.onEvent(AccountEvent.EnteredName("name"))
        val resultNameState = getCurrentAccountState().editName

        verifyAllMocks()
        assertThat(initialNameState).isEmpty()
        assertThat(resultNameState).isEqualTo("name")
    }

    @Test
    fun `EnteredName - initially not empty - changed string`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredName("name"))
        val initialNameState = getCurrentAccountState().editName

        accountViewModel.onEvent(AccountEvent.EnteredName("John4"))
        val resultNameState = getCurrentAccountState().editName

        verifyAllMocks()
        assertThat(initialNameState).isEqualTo("name")
        assertThat(resultNameState).isEqualTo("John4")
    }

    @Test
    fun `EnteredName - initially not empty - result empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredName("name"))
        val initialNameState = getCurrentAccountState().editName

        accountViewModel.onEvent(AccountEvent.EnteredName(""))
        val resultNameState = getCurrentAccountState().editName

        verifyAllMocks()
        assertThat(initialNameState).isEqualTo("name")
        assertThat(resultNameState).isEmpty()
    }

    @Test
    fun `EnteredPassword - initially empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val initialPasswordState = getCurrentAccountState().password

        accountViewModel.onEvent(AccountEvent.EnteredPassword("password"))
        val resultPasswordState = getCurrentAccountState().password

        verifyAllMocks()
        assertThat(initialPasswordState).isEmpty()
        assertThat(resultPasswordState).isEqualTo("password")
    }

    @Test
    fun `EnteredPassword - initially not empty - changed string`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentAccountState().password

        accountViewModel.onEvent(AccountEvent.EnteredPassword("Qwerty1+"))
        val resultPasswordState = getCurrentAccountState().password

        verifyAllMocks()
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEqualTo("Qwerty1+")
    }

    @Test
    fun `EnteredPassword - initially not empty - result empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredPassword("password"))
        val initialPasswordState = getCurrentAccountState().password

        accountViewModel.onEvent(AccountEvent.EnteredPassword(""))
        val resultPasswordState = getCurrentAccountState().password

        verifyAllMocks()
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(resultPasswordState).isEmpty()
    }

    @Test
    fun `EnteredConfirmPassword - initially empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val initialConfirmPasswordState = getCurrentAccountState().confirmPassword

        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("password"))
        val resultConfirmPasswordState = getCurrentAccountState().confirmPassword

        verifyAllMocks()
        assertThat(initialConfirmPasswordState).isEmpty()
        assertThat(resultConfirmPasswordState).isEqualTo("password")
    }

    @Test
    fun `EnteredConfirmPassword - initially not empty - changed string`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("password"))
        val initialConfirmPasswordState = getCurrentAccountState().confirmPassword

        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("Qwerty1+"))
        val resultConfirmPasswordState = getCurrentAccountState().confirmPassword

        verifyAllMocks()
        assertThat(initialConfirmPasswordState).isEqualTo("password")
        assertThat(resultConfirmPasswordState).isEqualTo("Qwerty1+")
    }

    @Test
    fun `EnteredConfirmPassword - initially not empty - result empty`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("password"))
        val initialConfirmPasswordState = getCurrentAccountState().confirmPassword

        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword(""))
        val resultConfirmPasswordState = getCurrentAccountState().confirmPassword

        verifyAllMocks()
        assertThat(initialConfirmPasswordState).isEqualTo("password")
        assertThat(resultConfirmPasswordState).isEmpty()
    }

    @Test
    fun `OnEditButtonClicked - edit dialog is opened`() {
        setMocks()
        accountViewModel = setViewModel()
        val initialEditDialogState = getCurrentAccountState().isEditDialogActivated

        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val resultEditDialogState = getCurrentAccountState().isEditDialogActivated

        verifyAllMocks()
        assertThat(initialEditDialogState).isFalse()
        assertThat(resultEditDialogState).isTrue()
    }

    @Test
    fun `OnDismiss - edit dialog is closed`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val initialEditDialogState = getCurrentAccountState().isEditDialogActivated

        accountViewModel.onEvent(AccountEvent.OnDismiss)
        val resultEditDialogState = getCurrentAccountState().isEditDialogActivated

        verifyAllMocks()
        assertThat(initialEditDialogState).isTrue()
        assertThat(resultEditDialogState).isFalse()
    }

    @Test
    fun `OnDismiss - state is reset`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredName("name"))
        accountViewModel.onEvent(AccountEvent.EnteredPassword("password"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("password"))
        val initialNameState = getCurrentAccountState().editName
        val initialPasswordState = getCurrentAccountState().password
        val initialConfirmPasswordState = getCurrentAccountState().confirmPassword

        accountViewModel.onEvent(AccountEvent.OnDismiss)
        val resultNameState = getCurrentAccountState().editName
        val resultPasswordState = getCurrentAccountState().password
        val resultConfirmPasswordState = getCurrentAccountState().confirmPassword

        verifyAllMocks()
        assertThat(initialNameState).isEqualTo("name")
        assertThat(initialPasswordState).isEqualTo("password")
        assertThat(initialConfirmPasswordState).isEqualTo("password")
        assertThat(resultNameState).isEmpty()
        assertThat(resultPasswordState).isEmpty()
        assertThat(resultConfirmPasswordState).isEmpty()
    }

    @Test
    fun `OnSave - name field is filled - edit dialog is closed`() {
        setMocks()
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredName(updatedUser.name))
        val initialEditDialogState = getCurrentAccountState().isEditDialogActivated

        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultEditDialogState = getCurrentAccountState().isEditDialogActivated

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserUseCase(updatedUser)
        }
        assertThat(initialEditDialogState).isTrue()
        assertThat(resultEditDialogState).isFalse()
    }

    @Test
    fun `OnSave - name field is filled - state is reset`() {
        setMocks()
        coEvery { updateUserUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredName(updatedUser.name))
        val initialNameState = getCurrentAccountState().editName

        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultNameState = getCurrentAccountState().editName

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserUseCase(updatedUser)
        }
        assertThat(initialNameState).isEqualTo(updatedUser.name)
        assertThat(resultNameState).isEmpty()
    }

    @Test
    fun `OnSave - password fields are filled - edit dialog is closed`() {
        setMocks()
        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("newPassword1+"))
        val initialEditDialogState = getCurrentAccountState().isEditDialogActivated

        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultEditDialogState = getCurrentAccountState().isEditDialogActivated

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserPasswordUseCase("newPassword1+")
        }
        assertThat(initialEditDialogState).isTrue()
        assertThat(resultEditDialogState).isFalse()
    }

    @Test
    fun `OnSave - password fields are filled - state is reset`() {
        setMocks()
        coEvery { updateUserPasswordUseCase(any()) } returns flowOf(Resource.Success(true))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        accountViewModel.onEvent(AccountEvent.EnteredPassword("newPassword1+"))
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("newPassword1+"))
        val initialPasswordState = getCurrentAccountState().password
        val initialConfirmPasswordState = getCurrentAccountState().confirmPassword

        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultPasswordState = getCurrentAccountState().password
        val resultConfirmPasswordState = getCurrentAccountState().confirmPassword

        verifyMocks()
        coVerifyOrder {
            getUserUseCase("userUID")
            getUserRecipesUseCase("userUID")
            updateUserPasswordUseCase("newPassword1+")
        }
        assertThat(initialPasswordState).isEqualTo("newPassword1+")
        assertThat(initialConfirmPasswordState).isEqualTo("newPassword1+")
        assertThat(resultPasswordState).isEmpty()
        assertThat(resultConfirmPasswordState).isEmpty()
    }

    @Test
    fun `OnSave - password is too short`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("Qwe"))
        val initialPasswordErrorState = getCurrentAccountState().passwordError

        accountViewModel.onEvent(AccountEvent.EnteredPassword("Qwe"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultPasswordErrorState = getCurrentAccountState().passwordError

        verifyAllMocks()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password is too short")
    }

    @Test
    fun `OnSave - password does not have at least one digit`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("Qwerty++"))
        val initialPasswordErrorState = getCurrentAccountState().passwordError

        accountViewModel.onEvent(AccountEvent.EnteredPassword("Qwerty++"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultPasswordErrorState = getCurrentAccountState().passwordError

        verifyAllMocks()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one digit")
    }

    @Test
    fun `OnSave - password does not have at least one capital letter`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("qwerty1+"))
        val initialPasswordErrorState = getCurrentAccountState().passwordError

        accountViewModel.onEvent(AccountEvent.EnteredPassword("qwerty1+"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultPasswordErrorState = getCurrentAccountState().passwordError

        verifyAllMocks()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one capital letter")
    }

    @Test
    fun `OnSave - password does not have at least one special character`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("Qwerty11"))
        val initialPasswordErrorState = getCurrentAccountState().passwordError

        accountViewModel.onEvent(AccountEvent.EnteredPassword("Qwerty11"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultPasswordErrorState = getCurrentAccountState().passwordError

        verifyAllMocks()
        assertThat(initialPasswordErrorState).isNull()
        assertThat(resultPasswordErrorState).isEqualTo("Password should have at least one special character")
    }

    @Test
    fun `OnSave - confirm password does not match`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.EnteredPassword("Qwerty11"))
        val initialConfirmPasswordErrorState = getCurrentAccountState().confirmPasswordError

        accountViewModel.onEvent(AccountEvent.EnteredConfirmPassword("Qwerty1+"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultConfirmPasswordErrorState = getCurrentAccountState().confirmPasswordError

        verifyAllMocks()
        assertThat(initialConfirmPasswordErrorState).isNull()
        assertThat(resultConfirmPasswordErrorState).isEqualTo("Passwords don't mach")
    }

    @Test
    fun `OnSave - name is too short`() {
        setMocks()
        accountViewModel = setViewModel()
        val initialNameErrorState = getCurrentAccountState().nameError

        accountViewModel.onEvent(AccountEvent.EnteredName("John4"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultNameErrorState = getCurrentAccountState().nameError

        verifyAllMocks()
        assertThat(initialNameErrorState).isNull()
        assertThat(resultNameErrorState).isEqualTo("Name is too short")
    }

    @Test
    fun `OnSave - name has at least one not allowed character`() {
        setMocks()
        accountViewModel = setViewModel()
        val initialNameErrorState = getCurrentAccountState().nameError

        accountViewModel.onEvent(AccountEvent.EnteredName("John@Smith4"))
        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultNameErrorState = getCurrentAccountState().nameError

        verifyAllMocks()
        assertThat(initialNameErrorState).isNull()
        assertThat(resultNameErrorState).isEqualTo("At least one character in name is not allowed")
    }

    @Test
    fun `OnSave - all fields empty - state doesn't change`() {
        setMocks()
        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnEditButtonClicked)
        val initialState = getCurrentAccountState()

        accountViewModel.onEvent(AccountEvent.OnSave)
        val resultState = getCurrentAccountState()

        verifyAllMocks()
        assertThat(initialState.isEditDialogActivated).isTrue()
        assertThat(initialState.editName).isEmpty()
        assertThat(initialState.nameError).isNull()
        assertThat(initialState.password).isEmpty()
        assertThat(initialState.passwordError).isNull()
        assertThat(initialState.confirmPassword).isEmpty()
        assertThat(initialState.confirmPasswordError).isNull()
        assertThat(resultState.isEditDialogActivated).isTrue()
        assertThat(resultState.editName).isEmpty()
        assertThat(resultState.nameError).isNull()
        assertThat(resultState.password).isEmpty()
        assertThat(resultState.passwordError).isNull()
        assertThat(resultState.confirmPassword).isEmpty()
        assertThat(resultState.confirmPasswordError).isNull()
    }
}