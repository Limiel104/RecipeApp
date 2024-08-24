package com.example.recipeapp.presentation.account

import com.example.recipeapp.domain.model.Recipe
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
    private lateinit var recipes: List<Recipe>
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

        recipes = listOf(
            Recipe(
                recipeId = "recipe2Id",
                name = "Recipe 2 Name",
                prepTime = "25 min",
                servings = 1,
                description = "Recipe 2 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image2Url",
                createdBy = "userId",
                categories = listOf("Category", "Category3"),
                date = 1234567891
            ),
            Recipe(
                recipeId = "recipe5Id",
                name = "Recipe 5 Name",
                prepTime = "15 min",
                servings = 1,
                description = "Recipe 5 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image5Url",
                createdBy = "userId",
                categories = listOf("Category1"),
                date = 1234567894
            ),
            Recipe(
                recipeId = "recipe4Id",
                name = "Recipe 4 Name",
                prepTime = "1 h 15 min",
                servings = 4,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image4Url",
                createdBy = "userId",
                categories = listOf("Category4","Category1","Category2"),
                date = 1234567893
            ),
            Recipe(
                recipeId = "recipeId",
                name = "Recipe Name",
                prepTime = "40 min",
                servings = 4,
                description = "Recipe description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "imageUrl",
                createdBy = "userId",
                categories = listOf("Category", "Category2", "Category3"),
                date = 1234567890
            ),
            Recipe(
                recipeId = "recipe6Id",
                name = "Recipe 6 Name",
                prepTime = "30 min",
                servings = 2,
                description = "Recipe 6 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image6Url",
                createdBy = "userId",
                categories = listOf("Category2"),
                date = 1234567895
            ),
            Recipe(
                recipeId = "recipe3Id",
                name = "Recipe 3 Name",
                prepTime = "1 h",
                servings = 6,
                description = "Recipe 3 description",
                isVegetarian = false,
                isVegan = false,
                imageUrl = "image3Url",
                createdBy = "userId",
                categories = listOf("Category4"),
                date = 1234567892
            )
        )
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

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
    fun `getUser sets recipes successfully`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).containsExactlyElementsIn(recipes)
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
    fun `updateUser sets recipes successfully`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        val result = getCurrentAccountState().recipes
        val isLoading = getCurrentAccountState().isLoading

        verifyAllMocks()
        assertThat(result).isEqualTo(recipes.sortedByDescending { it.date })
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in descending order`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateDescending))
        val result = getCurrentAccountState().recipes

        verifyAllMocks()
        assertThat(result).isEqualTo(recipes.sortedByDescending { it.date })
    }

    @Test
    fun `OnSortRecipes - recipes are sorted in ascending order`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

        accountViewModel = setViewModel()
        accountViewModel.onEvent(AccountEvent.OnSortRecipes(RecipeOrder.DateAscending))
        val result = getCurrentAccountState().recipes

        verifyAllMocks()
        assertThat(result).isEqualTo(recipes.sortedBy { it.date })
    }

    @Test
    fun `EnteredName - initially empty`() {
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))

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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
        coEvery { getUserUseCase(any()) } returns flowOf(Resource.Success(user))
        coEvery { getUserRecipesUseCase(any()) } returns flowOf(Resource.Success(recipes))
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
}