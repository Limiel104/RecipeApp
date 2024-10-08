package com.example.recipeapp.presentation.add_recipe

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.domain.model.RecipeWithIngredients
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddImageUseCase
import com.example.recipeapp.domain.use_case.AddRecipeUseCase
import com.example.recipeapp.domain.use_case.GetCategoriesUseCase
import com.example.recipeapp.domain.use_case.GetCurrentUserUseCase
import com.example.recipeapp.domain.use_case.GetIngredientsUseCase
import com.example.recipeapp.domain.use_case.ValidateFieldUseCase
import com.example.recipeapp.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coExcludeRecords
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddRecipeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var validateFieldUseCase: ValidateFieldUseCase
    private lateinit var getIngredientsUseCase: GetIngredientsUseCase
    private lateinit var addImageUseCase: AddImageUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var addRecipeUseCase: AddRecipeUseCase
    private lateinit var addRecipeViewModel: AddRecipeViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var ingredients: List<Ingredient>
    private lateinit var categories: List<Category>
    private lateinit var recipeWithIngredients: RecipeWithIngredients
    private lateinit var categoryMap: Map<Category, Boolean>

    @Before
    fun setUp() {
        validateFieldUseCase = ValidateFieldUseCase()
        getIngredientsUseCase = mockk()
        addImageUseCase = mockk()
        getCurrentUserUseCase = mockk()
        getCategoriesUseCase = mockk()
        addRecipeUseCase = mockk()
        firebaseUser = mockk()

        every { getCurrentUserUseCase() } returns firebaseUser
        every { firebaseUser.uid } returns "userUID"

        ingredients = listOf(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient 2 Name",
                imageUrl = "image2Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient 3 Name",
                imageUrl = "image3Url",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient4Id",
                name = "Ingredient 4 Name",
                imageUrl = "image4Url",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient5Id",
                name = "Ingredient 5 Name",
                imageUrl = "image5Url",
                category = "category5"
            )
        )

        categories = listOf(
            Category(
                categoryId = "category",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category2",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category3",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category4",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category5",
                imageUrl = "imageUrl"
            ),
            Category(
                categoryId = "category6",
                imageUrl = "imageUrl"
            )
        )

        recipeWithIngredients = RecipeWithIngredients(
            recipeId = "",
            name = "Recipe Name",
            ingredients = mapOf(
                ingredients[0] to "3 g",
                ingredients[1] to "5.6 kg"
            ),
            prepTime = "1 hour 40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = false,
            isVegan = false,
            imageUrl = "",
            createdBy = "userUID",
            categories = listOf("category", "category2", "category4"),
            date = 1234324354
        )

        categoryMap = categories.associateWith { false }
    }

    @After
    fun tearDown() {
        confirmVerified(getIngredientsUseCase)
        confirmVerified(addImageUseCase)
        confirmVerified(getCurrentUserUseCase)
        confirmVerified(getCategoriesUseCase)
        confirmVerified(addRecipeUseCase)
        confirmVerified(firebaseUser)
        clearAllMocks()
    }

    private fun setViewModel(): AddRecipeViewModel {
        return AddRecipeViewModel(
            validateFieldUseCase,
            getIngredientsUseCase,
            addImageUseCase,
            getCurrentUserUseCase,
            getCategoriesUseCase,
            addRecipeUseCase
        )
    }

    private fun getCurrentAddRecipeState(): AddRecipeState {
        return addRecipeViewModel.addRecipeState.value
    }

    private fun setMocks()  {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(categories))
    }

    private fun verifyMocks() {
        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
        }
    }

    @Test
    fun `enteredTitle - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialTitleState = getCurrentAddRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val resultTitleState = getCurrentAddRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEmpty()
        assertThat(resultTitleState).isEqualTo("title")
    }

    @Test
    fun `enteredTitle - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("old title"))
        val initialTitleState = getCurrentAddRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("new title"))
        val resultTitleState = getCurrentAddRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEqualTo("old title")
        assertThat(resultTitleState).isEqualTo("new title")
    }

    @Test
    fun `enteredTitle - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val initialTitleState = getCurrentAddRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle(""))
        val resultTitleState = getCurrentAddRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEqualTo("title")
        assertThat(resultTitleState).isEqualTo("")
    }

    @Test
    fun `enteredDescription - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDescriptionState = getCurrentAddRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val resultDescriptionState = getCurrentAddRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEmpty()
        assertThat(resultDescriptionState).isEqualTo("description")
    }

    @Test
    fun `enteredDescription - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("old description"))
        val initialDescriptionState = getCurrentAddRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("new description"))
        val resultDescriptionState = getCurrentAddRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEqualTo("old description")
        assertThat(resultDescriptionState).isEqualTo("new description")
    }

    @Test
    fun `enteredDescription - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialDescriptionState = getCurrentAddRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription(""))
        val resultDescriptionState = getCurrentAddRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEqualTo("description")
        assertThat(resultDescriptionState).isEqualTo("")
    }

    @Test
    fun `enteredIngredient - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialIngredientState = getCurrentAddRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("ingredient"))
        val resultIngredientState = getCurrentAddRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEmpty()
        assertThat(resultIngredientState).isEqualTo("ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("old ingredient"))
        val initialIngredientState = getCurrentAddRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("new ingredient"))
        val resultIngredientState = getCurrentAddRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("old ingredient")
        assertThat(resultIngredientState).isEqualTo("new ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("ingredient"))
        val initialIngredientState = getCurrentAddRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient(""))
        val resultIngredientState = getCurrentAddRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("ingredient")
        assertThat(resultIngredientState).isEqualTo("")
    }

    @Test
    fun `onAddRecipe - title is empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAddRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAddRecipeState().titleError

        verifyMocks()
        assertThat(initialTitleErrorState).isNull()
        assertThat(resultTitleErrorState).isEqualTo("Field can't be empty")
    }

    @Test
    fun `onAddRecipe - title is too short`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("ti"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAddRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAddRecipeState().titleError

        verifyMocks()
        assertThat(initialTitleErrorState).isNull()
        assertThat(resultTitleErrorState).isEqualTo("Field is too short")
    }

    @Test
    fun `onAddRecipe - title has at least one not allowed character`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title%"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAddRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAddRecipeState().titleError

        verifyMocks()
        assertThat(initialTitleErrorState).isNull()
        assertThat(resultTitleErrorState).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun `onAddRecipe - description is empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val initialDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        verifyMocks()
        assertThat(initialDescriptionErrorState).isNull()
        assertThat(resultDescriptionErrorState).isEqualTo("Field can't be empty")
    }

    @Test
    fun `onAddRecipe - description is too short`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("des"))
        val initialDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        verifyMocks()
        assertThat(initialDescriptionErrorState).isNull()
        assertThat(resultDescriptionErrorState).isEqualTo("Field is too short")
    }

    @Test
    fun `onAddRecipe - description has at least one not allowed character`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("desc_ription"))
        val initialDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        verifyMocks()
        assertThat(initialDescriptionErrorState).isNull()
        assertThat(resultDescriptionErrorState).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun `onAddRecipe - all fields are correct`() {
        setMocks()
        coEvery { addRecipeUseCase(any()) } returns flowOf(Resource.Success(true))

        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAddRecipeState().titleError
        val initialDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAddRecipeState().titleError
        val resultDescriptionErrorState = getCurrentAddRecipeState().descriptionError

        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
            getCurrentUserUseCase()
            firebaseUser.uid
            addRecipeUseCase(any())
        }
        assertThat(initialTitleErrorState).isNull()
        assertThat(resultTitleErrorState).isNull()
        assertThat(initialDescriptionErrorState).isNull()
        assertThat(resultDescriptionErrorState).isNull()
    }

    @Test
    fun `getIngredients runs successfully`() {
        setMocks()
        coExcludeRecords { getCategoriesUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().ingredientsToSelect
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(result).isEqualTo(ingredients)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Error("Error message"))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(categories))
        coExcludeRecords { getCategoriesUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().ingredientsToSelect
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getIngredients is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Loading(true))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Success(categories))
        coExcludeRecords { getCategoriesUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().ingredientsToSelect
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getCategories runs successfully`() {
        setMocks()
        coExcludeRecords { getIngredientsUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().categories
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEqualTo(categoryMap)
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getCategories returns error`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Error("Error message"))
        coExcludeRecords { getIngredientsUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().categories
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `getCategories is loading`() {
        coEvery { getIngredientsUseCase() } returns flowOf(Resource.Success(ingredients))
        coEvery { getCategoriesUseCase() } returns flowOf(Resource.Loading(true))
        coExcludeRecords { getIngredientsUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAddRecipeState().categories
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `onServingsButtonClicked - state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val resultServingsSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsSheetState).isFalse()
        assertThat(resultServingsSheetState).isTrue()
    }

    @Test
    fun `selectedServings - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsState = getCurrentAddRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(0)
        assertThat(resultServingsState).isEqualTo(2)
    }

    @Test
    fun `selectedServings - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAddRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(5))
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(5)
    }

    @Test
    fun `selectedServings - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAddRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(0))
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerSaved - isServingsBottomSheetOpened state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val initialServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsBottomSheetState).isTrue()
        assertThat(resultServingsBottomSheetState).isFalse()
    }

    @Test
    fun `onServingsPickerSaved - servings are not selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        verifyMocks()
        assertThat(initialLastSavedServingsState).isEqualTo(0)
        assertThat(resultLastSavedServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerSaved - servings are selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(3))
        val initialLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        verifyMocks()
        assertThat(initialLastSavedServingsState).isEqualTo(0)
        assertThat(resultLastSavedServingsState).isEqualTo(3)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsState = getCurrentAddRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(0)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 but selectedServings are not 0 - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAddRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 - bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val initialServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsBottomSheetState).isTrue()
        assertThat(resultServingsBottomSheetState).isFalse()
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is not 0 - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(3))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val initialServingsState = getCurrentAddRecipeState().selectedServings
        val initialLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(3)
        assertThat(initialLastSavedServingsState).isEqualTo(3)
        assertThat(resultServingsState).isEqualTo(3)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings and selectedServings are both not 0 but not the same - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(3))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(6))
        val initialServingsState = getCurrentAddRecipeState().selectedServings
        val initialLastSavedServingsState = getCurrentAddRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAddRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(6)
        assertThat(initialLastSavedServingsState).isEqualTo(3)
        assertThat(resultServingsState).isEqualTo(3)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is not 0 - bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(3))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val initialServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAddRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsBottomSheetState).isTrue()
        assertThat(resultServingsBottomSheetState).isFalse()
    }

    @Test
    fun `onPrepTimeButtonClicked - state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked)
        val resultPrepTimeSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        verifyMocks()
        assertThat(initialPrepTimeSheetState).isFalse()
        assertThat(resultPrepTimeSheetState).isTrue()
    }

    @Test
    fun `selectedPrepTimeHours - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("2 hours")
    }

    @Test
    fun `selectedPrepTimeHours - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("3 hours"))
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("2 hours")
        assertThat(resultPrepTimeHoursState).isEqualTo("3 hours")
    }

    @Test
    fun `selectedPrepTimeHours - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours(""))
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("2 hours")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeMinutesState).isEqualTo("15 min")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("35 min"))
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("15 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("35 min")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes(""))
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("15 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerSaved - isPrepTimeBottomSheetOpened state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked)
        val initialPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        verifyMocks()
        assertThat(initialPrepTimeBottomSheetState).isTrue()
        assertThat(resultPrepTimeBottomSheetState).isFalse()
    }

    @Test
    fun `onPrepTimePickerSaved - hours and minutes are not selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val resultLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        verifyMocks()
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeMinutesState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerSaved - hours and minutes are selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val resultLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        verifyMocks()
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultLastSavedPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerSaved - hours are selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val resultLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        verifyMocks()
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultLastSavedPrepTimeMinutesState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerSaved - minutes are selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val resultLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        verifyMocks()
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeHoursState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerSaved - hours and minutes are not selected - lastSavedPrepTime string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        verifyMocks()
        assertThat(initialLastSavedPrepTimeState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerSaved - hours and minutes are selected - lastSavedPrepTime string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        verifyMocks()
        assertThat(initialLastSavedPrepTimeState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeState).isEqualTo("1 hour 20 min")
    }

    @Test
    fun `onPrepTimePickerSaved - hours are selected - lastSavedPrepTime string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        verifyMocks()
        assertThat(initialLastSavedPrepTimeState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeState).isEqualTo("1 hour")
    }

    @Test
    fun `onPrepTimePickerSaved - minutes are selected - lastSavedPrepTime string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultLastSavedPrepTimeState = getCurrentAddRecipeState().lastSavedPrepTime

        verifyMocks()
        assertThat(initialLastSavedPrepTimeState).isEqualTo("")
        assertThat(resultLastSavedPrepTimeState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is empty - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("")
        assertThat(initialPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is empty but hours and minutes are not empty - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes


        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is empty but hours are not empty - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes


        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is empty but minutes are not empty - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes


        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("")
        assertThat(initialPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is empty - bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked)
        val initialPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        verifyMocks()
        assertThat(initialPrepTimeBottomSheetState).isTrue()
        assertThat(resultPrepTimeBottomSheetState).isFalse()
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is not empty - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime, hours and minutes are all not empty but not the same - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("3 hours"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("5 min"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("3 hours")
        assertThat(initialPrepTimeMinutesState).isEqualTo("5 min")
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime, hours and minutes are all not empty but hours are the same - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("5 min"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialPrepTimeMinutesState).isEqualTo("5 min")
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime, hours and minutes are all not empty but minutes are the same - reset to last saved`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("3 hours"))
        val initialPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val initialPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes
        val initialLastSavedPrepTimeHoursState = getCurrentAddRecipeState().lastSavedPrepTimeHours
        val initialLastSavedPrepTimeMinutesState = getCurrentAddRecipeState().lastSavedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed)
        val resultPrepTimeHoursState = getCurrentAddRecipeState().selectedPrepTimeHours
        val resultPrepTimeMinutesState = getCurrentAddRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("3 hours")
        assertThat(initialPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(initialLastSavedPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(initialLastSavedPrepTimeMinutesState).isEqualTo("20 min")
        assertThat(resultPrepTimeHoursState).isEqualTo("1 hour")
        assertThat(resultPrepTimeMinutesState).isEqualTo("20 min")
    }

    @Test
    fun `onPrepTimePickerDismissed - lastSavedPrepTime is not empty - bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("20 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked)
        val initialPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        val resultPrepTimeBottomSheetState = getCurrentAddRecipeState().isPrepTimeBottomSheetOpened

        verifyMocks()
        assertThat(initialPrepTimeBottomSheetState).isTrue()
        assertThat(resultPrepTimeBottomSheetState).isFalse()
    }

    @Test
    fun `onDropDownMenuExpandChange - initially false`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDropDownMenuExpandState = getCurrentAddRecipeState().isDropDownMenuExpanded

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropDownMenuExpandChange)
        val resultDropDownMenuExpandState = getCurrentAddRecipeState().isDropDownMenuExpanded

        verifyMocks()
        assertThat(initialDropDownMenuExpandState).isFalse()
        assertThat(resultDropDownMenuExpandState).isTrue()
    }

    @Test
    fun `onDropDownMenuExpandChange - initially true`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropDownMenuExpandChange)
        val initialDropDownMenuExpandState = getCurrentAddRecipeState().isDropDownMenuExpanded

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropDownMenuExpandChange)
        val resultDropDownMenuExpandState = getCurrentAddRecipeState().isDropDownMenuExpanded

        verifyMocks()
        assertThat(initialDropDownMenuExpandState).isTrue()
        assertThat(resultDropDownMenuExpandState).isFalse()
    }

    @Test
    fun `selectedIngredient - no recipe ingredients selected - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEmpty()
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - no recipe ingredients selected - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(ingredients)
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[1],
                ingredients[3],
                ingredients[4]
            )
        )
    }

    @Test
    fun `selectedIngredient - 2 out of 5 recipe ingredients selected initially - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],""),
                Pair(ingredients[1],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - 2 out of 5 recipe ingredients selected initially - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[2],
                ingredients[3]
            )
        )
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[3]
            )
        )
    }

    @Test
    fun `selectedIngredient - 4 out of 5 recipe ingredients selected initially - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[1],""),
                Pair(ingredients[3],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `selectedIngredient - 4 out of 5 recipe ingredients selected initially - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[2]
            )
        )
        assertThat(resultIngredientsState).isEqualTo(emptyList<Ingredient>())
    }

    @Test
    fun `onSwipeToDelete - 1 recipe ingredient selected initially - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(emptyMap<Ingredient, Quantity>())
    }

    @Test
    fun `onSwipeToDelete - 1 recipe ingredient selected initially - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[1],
                ingredients[2],
                ingredients[3]
            )
        )
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[0],
                ingredients[1],
                ingredients[2],
                ingredients[3],
                ingredients[4]
            )
        )
    }

    @Test
    fun `onSwipeToDelete - 3 recipe ingredient selected initially - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[3],"")
            )
        )
    }

    @Test
    fun `onSwipeToDelete - 3 recipe ingredient selected initially - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(
            listOf(
                ingredients[1],
                ingredients[2],
            )
        )
        assertThat(resultIngredientsState).isEqualTo(
            listOf(
                ingredients[1],
                ingredients[2],
                ingredients[4]
            )
        )
    }

    @Test
    fun `onSwipeToDelete - all recipe ingredient selected initially - recipe ingredients`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[3],""),
                Pair(ingredients[2],""),
                Pair(ingredients[1],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[0],""),
                Pair(ingredients[3],""),
                Pair(ingredients[2],""),
                Pair(ingredients[1],"")
            )
        )
    }

    @Test
    fun `onSwipeToDelete - all recipe ingredient selected initially - ingredients to select`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        val initialIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(ingredients[4]))
        val resultIngredientsState = getCurrentAddRecipeState().ingredientsToSelect

        verifyMocks()
        assertThat(initialIngredientsState).isEqualTo(emptyList<Ingredient>())
        assertThat(resultIngredientsState).isEqualTo(
            listOf(ingredients[4])
        )
    }

    @Test
    fun `onReorder - initially false`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialReorderModeState = getCurrentAddRecipeState().isReorderModeActivated

        addRecipeViewModel.onEvent(AddRecipeEvent.OnReorder)
        val resultReorderModeState = getCurrentAddRecipeState().isReorderModeActivated

        verifyMocks()
        assertThat(initialReorderModeState).isFalse()
        assertThat(resultReorderModeState).isTrue()
    }

    @Test
    fun `onReorder - initially true`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnReorder)
        val initialReorderModeState = getCurrentAddRecipeState().isReorderModeActivated

        addRecipeViewModel.onEvent(AddRecipeEvent.OnReorder)
        val resultReorderModeState = getCurrentAddRecipeState().isReorderModeActivated

        verifyMocks()
        assertThat(initialReorderModeState).isTrue()
        assertThat(resultReorderModeState).isFalse()
    }

    @Test
    fun `onDragIndexChange - initially not set - dragIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDragIndexState = getCurrentAddRecipeState().dragIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[2].ingredientId))
        val resultDragIndexState = getCurrentAddRecipeState().dragIndex

        verifyMocks()
        assertThat(initialDragIndexState).isEmpty()
        assertThat(resultDragIndexState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDragIndexChange - initially not set - dropIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDropIndexState = getCurrentAddRecipeState().dropIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[2].ingredientId))
        val resultDropIndexState = getCurrentAddRecipeState().dropIndex

        verifyMocks()
        assertThat(initialDropIndexState).isEmpty()
        assertThat(resultDropIndexState).isEmpty()
    }

    @Test
    fun `onDragIndexChange - initially set - dragIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[3].ingredientId))
        val initialDragIndexState = getCurrentAddRecipeState().dragIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[2].ingredientId))
        val resultDragIndexState = getCurrentAddRecipeState().dragIndex

        verifyMocks()
        assertThat(initialDragIndexState).isEqualTo(ingredients[3].ingredientId)
        assertThat(resultDragIndexState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDragIndexChange - initially set - dropIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[3].ingredientId))
        val initialDropIndexState = getCurrentAddRecipeState().dropIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDragIndexChange(ingredients[2].ingredientId))
        val resultDropIndexState = getCurrentAddRecipeState().dropIndex

        verifyMocks()
        assertThat(initialDropIndexState).isEmpty()
        assertThat(resultDropIndexState).isEmpty()
    }

    @Test
    fun `onDropIndexChange - initially not set - dropIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDropIndexState = getCurrentAddRecipeState().dropIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[2].ingredientId))
        val resultDropIndexState = getCurrentAddRecipeState().dropIndex

        verifyMocks()
        assertThat(initialDropIndexState).isEmpty()
        assertThat(resultDropIndexState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDropIndexChange - initially not set - dragIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDragIndexState = getCurrentAddRecipeState().dragIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[2].ingredientId))
        val resultDragIndexState = getCurrentAddRecipeState().dragIndex

        verifyMocks()
        assertThat(initialDragIndexState).isEmpty()
        assertThat(resultDragIndexState).isEmpty()
    }

    @Test
    fun `onDropIndexChange - initially set - dropIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[3].ingredientId))
        val initialDropIndexState = getCurrentAddRecipeState().dropIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[2].ingredientId))
        val resultDropIndexState = getCurrentAddRecipeState().dropIndex

        verifyMocks()
        assertThat(initialDropIndexState).isEqualTo(ingredients[3].ingredientId)
        assertThat(resultDropIndexState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDropIndexChange - initially set - dragIndex`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[3].ingredientId))
        val initialDragIndexState = getCurrentAddRecipeState().dragIndex

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[2].ingredientId))
        val resultDragIndexState = getCurrentAddRecipeState().dragIndex

        verifyMocks()
        assertThat(initialDragIndexState).isEmpty()
        assertThat(resultDragIndexState).isEmpty()
    }

    @Test
    fun `onDraggedIngredientChange - initially not set - reordered ingredient id is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDraggedIngredientIdState = getCurrentAddRecipeState().draggedIngredientId

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultDraggedIngredientIdState = getCurrentAddRecipeState().draggedIngredientId

        verifyMocks()
        assertThat(initialDraggedIngredientIdState).isEmpty()
        assertThat(resultDraggedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDraggedIngredientChange - initially set - reordered ingredient id is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[3].ingredientId))
        val initialDraggedIngredientIdState = getCurrentAddRecipeState().draggedIngredientId

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultDraggedIngredientIdState = getCurrentAddRecipeState().draggedIngredientId

        verifyMocks()
        assertThat(initialDraggedIngredientIdState).isEqualTo(ingredients[3].ingredientId)
        assertThat(resultDraggedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onDraggedIngredientChange - reorder correct when 1 recipe ingredient selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[2].ingredientId))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `onDraggedIngredientChange - reorder correct when 2 recipe ingredients selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[3].ingredientId))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[3],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `onDraggedIngredientChange - reorder correct when 3 recipe ingredients selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[3].ingredientId))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],""),
                Pair(ingredients[4],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[4],""),
                Pair(ingredients[3],""),
                Pair(ingredients[2],"")
            )
        )
    }

    @Test
    fun `onDraggedIngredientChange - reorder correct when all recipe ingredients selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDropIndexChange(ingredients[1].ingredientId))
        val initialRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(ingredients[2].ingredientId))
        val resultRecipeIngredientsState = getCurrentAddRecipeState().recipeIngredients

        verifyMocks()
        assertThat(initialRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[1],""),
                Pair(ingredients[0],""),
                Pair(ingredients[2],""),
                Pair(ingredients[4],""),
                Pair(ingredients[3],"")
            )
        )
        assertThat(resultRecipeIngredientsState).isEqualTo(
            mapOf(
                Pair(ingredients[2],""),
                Pair(ingredients[1],""),
                Pair(ingredients[0],""),
                Pair(ingredients[4],""),
                Pair(ingredients[3],"")
            )
        )
    }

    @Test
    fun `onIngredientClicked - initially not selected - selected ingredient`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialSelectedIngredientIdState = getCurrentAddRecipeState().selectedIngredientId

        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultSelectedIngredientIdState = getCurrentAddRecipeState().selectedIngredientId

        verifyMocks()
        assertThat(initialSelectedIngredientIdState).isEqualTo("")
        assertThat(resultSelectedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onIngredientClicked - initially selected - selected ingredient`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[1].ingredientId))
        val initialSelectedIngredientIdState = getCurrentAddRecipeState().selectedIngredientId

        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultSelectedIngredientIdState = getCurrentAddRecipeState().selectedIngredientId

        verifyMocks()
        assertThat(initialSelectedIngredientIdState).isEqualTo(ingredients[1].ingredientId)
        assertThat(resultSelectedIngredientIdState).isEqualTo(ingredients[2].ingredientId)
    }

    @Test
    fun `onIngredientClicked - is quantity bottom sheet opened after click`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialQuantityBottomSheetState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val resultQuantityBottomSheetState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isFalse()
        assertThat(resultQuantityBottomSheetState).isTrue()
    }

    @Test
    fun `selectedWholeQuantity - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("350"))
        val resultWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("")
        assertThat(resultWholeQuantityState).isEqualTo("350")
    }

    @Test
    fun `selectedWholeQuantity - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("350"))
        val initialWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("45"))
        val resultWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("350")
        assertThat(resultWholeQuantityState).isEqualTo("45")
    }

    @Test
    fun `selectedWholeQuantity - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("350"))
        val initialWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("0"))
        val resultWholeQuantityState = getCurrentAddRecipeState().selectedWholeQuantity

        verifyMocks()
        assertThat(initialWholeQuantityState).isEqualTo("350")
        assertThat(resultWholeQuantityState).isEqualTo("0")
    }

    @Test
    fun `selectedDecimalQuantity - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".7"))
        val resultDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo("")
        assertThat(resultDecimalQuantityState).isEqualTo(".7")
    }

    @Test
    fun `selectedDecimalQuantity - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".7"))
        val initialDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".3"))
        val resultDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo(".7")
        assertThat(resultDecimalQuantityState).isEqualTo(".3")
    }

    @Test
    fun `selectedDecimalQuantity - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".7"))
        val initialDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        val resultDecimalQuantityState = getCurrentAddRecipeState().selectedDecimalQuantity

        verifyMocks()
        assertThat(initialDecimalQuantityState).isEqualTo(".7")
        assertThat(resultDecimalQuantityState).isEqualTo(".0")
    }

    @Test
    fun `selectedTypeQuantity - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("bowl"))
        val resultTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("")
        assertThat(resultTypeQuantityState).isEqualTo("bowl")
    }

    @Test
    fun `selectedTypeQuantity - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("bowl"))
        val initialTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("handful"))
        val resultTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("bowl")
        assertThat(resultTypeQuantityState).isEqualTo("handful")
    }

    @Test
    fun `selectedTypeQuantity - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("bowl"))
        val initialTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("-"))
        val resultTypeQuantityState = getCurrentAddRecipeState().selectedTypeQuantity

        verifyMocks()
        assertThat(initialTypeQuantityState).isEqualTo("bowl")
        assertThat(resultTypeQuantityState).isEqualTo("-")
    }

    @Test
    fun `onQuantityPickerSaved - isQuantityBottomSheetOpened state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialQuantityBottomSheetState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultQuantityBottomSheetState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isTrue()
        assertThat(resultQuantityBottomSheetState).isFalse()
    }

    @Test
    fun `onQuantityPickerSaved - quantity is not selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerSaved - quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerSaved - whole and decimal quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5")
    }

    @Test
    fun `onQuantityPickerSaved - whole and type quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30 g")
    }

    @Test
    fun `onQuantityPickerSaved - decimal and type quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo(".5 g")
    }

    @Test
    fun `onQuantityPickerSaved - only whole quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("30")
    }

    @Test
    fun `onQuantityPickerSaved - only decimal quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo(".5")
    }

    @Test
    fun `onQuantityPickerSaved - only type quantity is selected - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("g")
    }

    @Test
    fun `onQuantityPickerSaved - quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("12"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12.0 kg")
    }

    @Test
    fun `onQuantityPickerSaved - whole and decimal quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("12"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12.0")
    }

    @Test
    fun `onQuantityPickerSaved - whole and type quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("12"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12 kg")
    }

    @Test
    fun `onQuantityPickerSaved - decimal and type quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo(".0 kg")
    }

    @Test
    fun `onQuantityPickerSaved - only whole quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("12"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("12")
    }

    @Test
    fun `onQuantityPickerSaved - only decimal quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo(".0")
    }

    @Test
    fun `onQuantityPickerSaved - only type quantity is selected - initially not empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("kg")
    }

    @Test
    fun `onQuantityPickerSaved - quantity initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity(""))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(""))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity(""))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity not set - still default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity set - value not changed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity not set and then changed - still default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("15"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("")
        assertThat(resultIngredientQuantityState).isEqualTo("")
    }

    @Test
    fun `onQuantityPickerDismissed - ingredient quantity set and then changed - value not changed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("30"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        val initialIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("15"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".0"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentAddRecipeState().recipeIngredients[ingredients[2]]

        verifyMocks()
        assertThat(initialIngredientQuantityState).isEqualTo("30.5 g")
        assertThat(resultIngredientQuantityState).isEqualTo("30.5 g")
    }

    @Test
    fun `onQuantityPickerDismissed - quantity bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[2].ingredientId))
        val initialQuantityBottomSheetState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed)
        val resultIngredientQuantityState = getCurrentAddRecipeState().isQuantityBottomSheetOpened

        verifyMocks()
        assertThat(initialQuantityBottomSheetState).isTrue()
        assertThat(resultIngredientQuantityState).isFalse()
    }

    @Test
    fun `onCategoriesButtonClicked - categories dialog is state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesDialogState = getCurrentAddRecipeState().isCategoriesDialogActivated

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCategoriesButtonClicked)
        val resultCategoriesDialogState = getCurrentAddRecipeState().isCategoriesDialogActivated

        verifyMocks()
        assertThat(initialCategoriesDialogState).isFalse()
        assertThat(resultCategoriesDialogState).isTrue()
    }

    @Test
    fun `onCheckBoxToggled - toggled one category - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],true),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - toggled one category - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onCheckBoxToggled - toggled 2 out of 6 categories - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],true),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - toggled 2 out of 6 categories - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onCheckBoxToggled - toggled all categories - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onCheckBoxToggled - toggled all categories - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onCheckBoxToggled - untoggled one category - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - untoggled one category - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onCheckBoxToggled - untoggled 2 out of 6 categories - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onCheckBoxToggled - untoggled 2 out of 6 categories - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onCheckBoxToggled - untoggled all categories - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onCheckBoxToggled - untoggled all categories - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onDialogSave - initially empty - selected one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected three - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],true),
                Pair(categories[2],false),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected three - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],true),
                Pair(categories[2],false),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected all - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onDialogSave - initially empty - selected all - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onDialogSave - initially not empty - selected one more - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - selected one more - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - selected two more - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - selected two more - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - selected all left - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],true),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - selected all left - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],true),
                Pair(categories[2],true),
                Pair(categories[3],true),
                Pair(categories[4],true),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected and unselected one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially empty - selected and unselected one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially empty - selected and unselected three - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially empty - selected and unselected three - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially empty - selected two unselect one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],true),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected two unselect one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],false),
                Pair(categories[3],true),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected three unselect two - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially empty - selected three unselected two - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - unselected one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - unselected one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - unselected two - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - unselected two - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],false),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],false)
            )
        )
    }

    @Test
    fun `onDialogSave - initially not empty - unselected all - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially not empty - unselected all - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(
            mapOf(
                Pair(categories[0],true),
                Pair(categories[1],false),
                Pair(categories[2],true),
                Pair(categories[3],false),
                Pair(categories[4],false),
                Pair(categories[5],true)
            )
        )
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially all selected - unselected all - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - initially all selected - unselected all - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogSave - dialog is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCategoriesButtonClicked)
        val initialCategoriesDialogActivatedState = getCurrentAddRecipeState().isCategoriesDialogActivated

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val resultCategoriesDialogActivatedState = getCurrentAddRecipeState().isCategoriesDialogActivated

        verifyMocks()
        assertThat(initialCategoriesDialogActivatedState).isTrue()
        assertThat(resultCategoriesDialogActivatedState).isFalse()
    }

    @Test
    fun `onDialogDismiss - initially empty - selected one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected three - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected three - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected all - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected all - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected one more - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected one more - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected two more - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected two more - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected all left - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - selected all left - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially empty - selected and unselected one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected and unselected one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected and unselected three - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected and unselected three - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected two unselect one - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected two unselect one - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected three unselect two - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially empty - selected three unselected two - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { false })
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected one - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected one - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected two - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected two - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected all - categories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially not empty - unselected all - lastSavedCategories`() {
        val recipeCategories = mapOf(
            Pair(categories[0],true),
            Pair(categories[1],false),
            Pair(categories[2],true),
            Pair(categories[3],false),
            Pair(categories[4],false),
            Pair(categories[5],true)
        )

        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(recipeCategories)
        assertThat(resultLastSavedCategoriesState).isEqualTo(recipeCategories)
    }

    @Test
    fun `onDialogDismiss - initially all selected - unselected all - categories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialCategoriesState = getCurrentAddRecipeState().categories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesState = getCurrentAddRecipeState().categories

        verifyMocks()
        assertThat(initialCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onDialogDismiss - initially all selected - unselected all - lastSavedCategories`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)
        val initialLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[2]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[4]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[5]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultLastSavedCategoriesState = getCurrentAddRecipeState().lastSavedCategories

        verifyMocks()
        assertThat(initialLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
        assertThat(resultLastSavedCategoriesState).isEqualTo(categories.associateWith { true })
    }

    @Test
    fun `onDialogDismissed - dialog is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCategoriesButtonClicked)
        val initialCategoriesDialogActivatedState = getCurrentAddRecipeState().isCategoriesDialogActivated

        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogDismiss)
        val resultCategoriesDialogActivatedState = getCurrentAddRecipeState().isCategoriesDialogActivated

        verifyMocks()
        assertThat(initialCategoriesDialogActivatedState).isTrue()
        assertThat(resultCategoriesDialogActivatedState).isFalse()
    }

    @Test
    fun `onAddImage - image bottom sheet is opened`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddImage)
        val resultImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        verifyMocks()
        assertThat(initialImageBottomSheetState).isFalse()
        assertThat(resultImageBottomSheetState).isTrue()
    }

    @Test
    fun `onTakePhoto - image bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddImage)
        val initialImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnTakePhoto)
        val resultImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        verifyMocks()
        assertThat(initialImageBottomSheetState).isTrue()
        assertThat(resultImageBottomSheetState).isFalse()
    }

    @Test
    fun `onSelectImage - image bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddImage)
        val initialImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnSelectImage)
        val resultImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        verifyMocks()
        assertThat(initialImageBottomSheetState).isTrue()
        assertThat(resultImageBottomSheetState).isFalse()
    }

    @Test
    fun `onAddImageDismiss - image bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddImage)
        val initialImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddImageDismiss)
        val resultImageBottomSheetState = getCurrentAddRecipeState().isImageBottomSheetOpened

        verifyMocks()
        assertThat(initialImageBottomSheetState).isTrue()
        assertThat(resultImageBottomSheetState).isFalse()
    }

    @Test
    fun `addRecipe runs successfully`() {
        setMocks()
        coEvery { addRecipeUseCase(any()) } returns flowOf(Resource.Success(true))

        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("Recipe Name"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("Recipe description"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
            getCurrentUserUseCase()
            firebaseUser.uid
            addRecipeUseCase(any())
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `addRecipe returns error`() {
        setMocks()
        coEvery { addRecipeUseCase(any()) } returns flowOf(Resource.Error("Error message"))

        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("Recipe Name"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("Recipe description"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
            getCurrentUserUseCase()
            firebaseUser.uid
            addRecipeUseCase(any())
        }
        assertThat(isLoading).isFalse()
    }

    @Test
    fun `addRecipe is loading`() {
        setMocks()
        coEvery { addRecipeUseCase(any()) } returns flowOf(Resource.Loading(true))

        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("Recipe Name"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("Recipe description"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
            getCurrentUserUseCase()
            firebaseUser.uid
            addRecipeUseCase(any())
        }
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `addRecipe - passes recipe correctly`() {
        setMocks()
        val capturedRecipe = slot<RecipeWithIngredients>()
        coEvery { addRecipeUseCase(capture(capturedRecipe)) } returns flowOf(Resource.Success(true))


        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("Recipe Name"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("Recipe description"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[0].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("3"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("g"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedIngredient(ingredients[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnIngredientClicked(ingredients[1].ingredientId))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity("5"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(".6"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity("kg"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(4))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("1 hour"))
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("40 min"))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved)
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[1]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[0]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnCheckBoxToggled(categories[3]))
        addRecipeViewModel.onEvent(AddRecipeEvent.OnDialogSave)

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val isLoading = getCurrentAddRecipeState().isLoading

        coVerifySequence {
            getIngredientsUseCase()
            getCategoriesUseCase()
            getCurrentUserUseCase()
            firebaseUser.uid
            addRecipeUseCase(any())
        }
        assertThat(isLoading).isFalse()
        assertThat(capturedRecipe.isCaptured).isTrue()
        assertThat(capturedRecipe.captured.name).isEqualTo("Recipe Name")
        assertThat(capturedRecipe.captured.description).isEqualTo("Recipe description")
        assertThat(capturedRecipe.captured.ingredients).isEqualTo(mapOf(
            Pair(ingredients[0],"3 g"),
            Pair(ingredients[1],"5.6 kg")
        ))
        assertThat(capturedRecipe.captured.servings).isEqualTo(4)
        assertThat(capturedRecipe.captured.prepTime).isEqualTo("1 hour 40 min")
        assertThat(capturedRecipe.captured.categories).containsExactlyElementsIn(listOf(
            categories[1].categoryId,
            categories[0].categoryId,
            categories[3].categoryId
        ))
    }
}