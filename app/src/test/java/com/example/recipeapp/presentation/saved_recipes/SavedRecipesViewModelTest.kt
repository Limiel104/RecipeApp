package com.example.recipeapp.presentation.saved_recipes

import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddSearchSuggestionUseCase
import com.example.recipeapp.domain.use_case.DeleteSavedRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetSavedRecipeIdUseCase
import com.example.recipeapp.domain.use_case.GetSearchSuggestionsUseCase
import com.example.recipeapp.domain.use_case.GetUserSavedRecipesUseCase
import com.example.recipeapp.domain.use_case.SortRecipesUseCase
import com.example.recipeapp.domain.util.RecipeOrder
import com.example.recipeapp.presentation.common.getRecipes
import com.example.recipeapp.presentation.common.getSearchSuggestions
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedRecipesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getUserSavedRecipesUseCase: GetUserSavedRecipesUseCase
    private lateinit var deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
    private lateinit var getSavedRecipeIdUseCase: GetSavedRecipeIdUseCase
    private lateinit var addSearchSuggestionUseCase: AddSearchSuggestionUseCase
    private lateinit var getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase
    private lateinit var sortRecipesUseCase: SortRecipesUseCase
    private lateinit var savedRecipesViewModel: SavedRecipesViewModel
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        getCurrentUserUseCase = mockk()
        getUserSavedRecipesUseCase = mockk()
        deleteSavedRecipeUseCase = mockk()
        getSavedRecipeIdUseCase = mockk()
        addSearchSuggestionUseCase = mockk()
        getSearchSuggestionsUseCase = mockk()
        sortRecipesUseCase = SortRecipesUseCase()
        firebaseUser = mockk()

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"
    }

    @After
    fun tearDown() {
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(getUserSavedRecipesUseCase)
        confirmVerified(deleteSavedRecipeUseCase)
        confirmVerified(getSavedRecipeIdUseCase)
        confirmVerified(addSearchSuggestionUseCase)
        confirmVerified(getSearchSuggestionsUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): SavedRecipesViewModel {
        return SavedRecipesViewModel(
            getCurrentUserUseCase,
            getUserSavedRecipesUseCase,
            deleteSavedRecipeUseCase,
            getSavedRecipeIdUseCase,
            addSearchSuggestionUseCase,
            getSearchSuggestionsUseCase,
            sortRecipesUseCase
        )
    }

    private fun getCurrentSavedRecipesState(): SavedRecipesState {
        return savedRecipesViewModel.savedRecipesState.value
    }

    private fun setMocks() {
        coEvery {
            getUserSavedRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Success(getRecipes()))
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
            getUserSavedRecipesUseCase("userUID", "", true)
        }
    }

    @Test
    fun `checkIfUserLoggedIn - user is logged in`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().isUserLoggedIn

        verifyAllMocks()
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `checkIfUserLoggedIn - user is not logged in`() {
        every { getCurrentUserUseCase() } returns null

        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().isUserLoggedIn

        verifySequence { getCurrentUserUseCase() }
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `getUserSavedRecipes sets recipes successfully`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().savedRecipes
        val isLoading = getCurrentSavedRecipesState().isLoading

        verifyAllMocks()
        Truth.assertThat(result).containsExactlyElementsIn(getRecipes())
        Truth.assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserSavedRecipes returns error`() {
        coEvery {
            getUserSavedRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Error("Error message"))

        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().savedRecipes
        val isLoading = getCurrentSavedRecipesState().isLoading

        verifyAllMocks()
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isFalse()
    }

    @Test
    fun `getUserSavedRecipes is loading`() {
        coEvery {
            getUserSavedRecipesUseCase(any(), any(), any())
        } returns flowOf(Resource.Loading(true))

        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().savedRecipes
        val isLoading = getCurrentSavedRecipesState().isLoading

        verifyAllMocks()
        Truth.assertThat(result).isEmpty()
        Truth.assertThat(isLoading).isTrue()
    }

    @Test
    fun `getUserSavedRecipes - recipes are sorted correctly`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        val result = getCurrentSavedRecipesState().savedRecipes

        verifyAllMocks()
        Truth.assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
    }

    @Test
    fun `getSavedRecipeId is successful`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(any()) } returns flowOf(Resource.Loading(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))

        verifyAllMocks()
        coVerifyOrder {
            getSavedRecipeIdUseCase("userUID","recipeId")
            deleteSavedRecipeUseCase("savedRecipeId")
        }
    }

    @Test
    fun `getSavedRecipeId returns error`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Error("Error message"))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))

        verifyAllMocks()
        coVerifyOrder {
            getSavedRecipeIdUseCase("userUID","recipeId")
        }
    }

    @Test
    fun `getSavedRecipeId is loading`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Loading(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))
        val isLoading = getCurrentSavedRecipesState().isLoading

        verifyAllMocks()
        coVerifyOrder {
            getSavedRecipeIdUseCase("userUID", "recipeId")
        }
        Truth.assertThat(isLoading).isTrue()
    }

    @Test
    fun `deleteSavedRecipe is successful`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(any()) } returns flowOf(Resource.Success(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSavedRecipeIdUseCase("userUID","recipeId")
            deleteSavedRecipeUseCase("savedRecipeId")
            getUserSavedRecipesUseCase("userUID","",true)
        }
    }

    @Test
    fun `deleteSavedRecipe returns error`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSavedRecipeIdUseCase("userUID","recipeId")
            deleteSavedRecipeUseCase("savedRecipeId")
        }
    }

    @Test
    fun `deleteSavedRecipe is loading`() {
        setMocks()
        coEvery { getSavedRecipeIdUseCase(any(), any()) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(any()) } returns flowOf(Resource.Loading(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))
        val isLoading = getCurrentSavedRecipesState().isLoading

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSavedRecipeIdUseCase("userUID","recipeId")
            deleteSavedRecipeUseCase("savedRecipeId")
        }
        Truth.assertThat(isLoading).isTrue()
    }

    @Test
    fun `onRemove - arguments passed correctly`() {
        val userUID = slot<String>()
        val recipeId = slot<String>()
        val savedRecipeId = slot<String>()

        setMocks()
        coEvery { getSavedRecipeIdUseCase(capture(userUID), capture(recipeId)) } returns flowOf(Resource.Success("savedRecipeId"))
        coEvery { deleteSavedRecipeUseCase(capture(savedRecipeId)) } returns flowOf(Resource.Success(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnRemove("recipeId"))

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSavedRecipeIdUseCase("userUID","recipeId")
            deleteSavedRecipeUseCase("savedRecipeId")
            getUserSavedRecipesUseCase("userUID","",true)
        }
        Truth.assertThat(userUID.captured).isEqualTo("userUID")
        Truth.assertThat(recipeId.captured).isEqualTo("recipeId")
        Truth.assertThat(savedRecipeId.captured).isEqualTo("savedRecipeId")
    }

    @Test
    fun `onQueryChanged - initial query empty`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        val initialQueryState = getCurrentSavedRecipesState().query

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentSavedRecipesState().query

        verifyAllMocks()
        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onQueryChanged - initial query not empty`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnQueryChange("Old Query"))
        val initialQueryState = getCurrentSavedRecipesState().query

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnQueryChange("New Query"))
        val resultQueryState = getCurrentSavedRecipesState().query

        verifyAllMocks()
        Truth.assertThat(initialQueryState).isEqualTo("Old Query")
        Truth.assertThat(resultQueryState).isEqualTo("New Query")
    }

    @Test
    fun `onSortRecipes - recipes are sorted in descending order`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSortRecipes(RecipeOrder.DateDescending))
        val result = getCurrentSavedRecipesState().savedRecipes

        verifyAllMocks()
        Truth.assertThat(result).isEqualTo(getRecipes().sortedByDescending { it.date })
    }

    @Test
    fun `onSortRecipes - recipes are sorted in ascending order`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSortRecipes(RecipeOrder.DateAscending))
        val result = getCurrentSavedRecipesState().savedRecipes

        verifyAllMocks()
        Truth.assertThat(result).isEqualTo(getRecipes().sortedBy { it.date })
    }

    @Test
    fun `onActiveChange - initially false`() {
        setMocks()
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(getSearchSuggestions()))

        savedRecipesViewModel = setViewModel()
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive
        val initialSearchSuggestions = getCurrentSavedRecipesState().searchSuggestions

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnActiveChange)
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive
        val resultSearchSuggestions = getCurrentSavedRecipesState().searchSuggestions

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(),any(),any())
            getSearchSuggestionsUseCase()
        }
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(initialSearchSuggestions).isEmpty()
        Truth.assertThat(resultActiveState).isTrue()
        Truth.assertThat(resultSearchSuggestions).isEqualTo(getSearchSuggestions())
    }

    @Test
    fun `onActiveChange - initially true`() {
        setMocks()
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success(getSearchSuggestions()))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnActiveChange)
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnActiveChange)
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive

        coVerifySequence {
            getCurrentUserUseCase()
            firebaseUser.uid
            firebaseUser.uid
            getUserSavedRecipesUseCase(any(),any(),any())
            getSearchSuggestionsUseCase()
        }
        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onSearchClicked - isActive false`() {
        setMocks()
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))

        savedRecipesViewModel = setViewModel()
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSearchClicked)
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            addSearchSuggestionUseCase(any())
            getUserSavedRecipesUseCase("userUID","",true)
        }
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onSearchClicked - isActive true`() {
        setMocks()
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success( getSearchSuggestions()))
        coEvery { addSearchSuggestionUseCase(any()) } returns flowOf(Resource.Success(true))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnActiveChange)
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSearchClicked)
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSearchSuggestionsUseCase()
            addSearchSuggestionUseCase(any())
            getUserSavedRecipesUseCase("userUID","",true)
        }
        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onClearClicked - query empty and search is active`() {
        setMocks()
        coEvery { getSearchSuggestionsUseCase() } returns flowOf(Resource.Success( getSearchSuggestions()))

        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnActiveChange)
        val initialQueryState = getCurrentSavedRecipesState().query
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnClearClicked)
        val resultQueryState = getCurrentSavedRecipesState().query
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive

        verifyMocks()
        coVerifyOrder {
            getUserSavedRecipesUseCase("userUID","",true)
            getSearchSuggestionsUseCase()
            getUserSavedRecipesUseCase("userUID","",true)
        }
        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(initialActiveState).isTrue()
        Truth.assertThat(resultQueryState).isEmpty()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onClearClicked - query not empty`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnQueryChange("Initial query"))
        val initialQueryState = getCurrentSavedRecipesState().query
        val initialActiveState = getCurrentSavedRecipesState().isSearchActive

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnClearClicked)
        val resultQueryState = getCurrentSavedRecipesState().query
        val resultActiveState = getCurrentSavedRecipesState().isSearchActive

        verifyAllMocks()
        Truth.assertThat(initialQueryState).isEqualTo("Initial query")
        Truth.assertThat(initialActiveState).isFalse()
        Truth.assertThat(resultQueryState).isEmpty()
        Truth.assertThat(resultActiveState).isFalse()
    }

    @Test
    fun `onSearchSuggestionClicked - initial query is empty`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        val initialQueryState = getCurrentSavedRecipesState().query

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentSavedRecipesState().query

        verifyAllMocks()
        Truth.assertThat(initialQueryState).isEmpty()
        Truth.assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }

    @Test
    fun `onSearchSuggestionClicked - initial query is not empty`() {
        setMocks()
        savedRecipesViewModel = setViewModel()
        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnQueryChange("Initial query"))
        val initialQueryState = getCurrentSavedRecipesState().query

        savedRecipesViewModel.onEvent(SavedRecipesEvent.OnSearchSuggestionClicked("Suggestion Text"))
        val resultQueryState = getCurrentSavedRecipesState().query

        verifyAllMocks()
        Truth.assertThat(initialQueryState).isEqualTo("Initial query")
        Truth.assertThat(resultQueryState).isEqualTo("Suggestion Text")
    }
}