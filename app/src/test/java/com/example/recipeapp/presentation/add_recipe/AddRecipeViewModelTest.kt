package com.example.recipeapp.presentation.add_recipe

import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
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
            recipeId = "recipeId",
            name = "Recipe Name",
            ingredients = mapOf(
                ingredients[0] to "3 g",
                ingredients[1] to "5 g"
            ),
            prepTime = "40 min",
            servings = 4,
            description = "Recipe description",
            isVegetarian = true,
            isVegan = false,
            imageUrl = "imageUrl",
            createdBy = "userUID",
            categories = listOf("category", "category2", "category4")
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

    private fun getCurrentAdRecipeState(): AddRecipeState {
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
        val initialTitleState = getCurrentAdRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val resultTitleState = getCurrentAdRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEmpty()
        assertThat(resultTitleState).isEqualTo("title")
    }

    @Test
    fun `enteredTitle - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("old title"))
        val initialTitleState = getCurrentAdRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("new title"))
        val resultTitleState = getCurrentAdRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEqualTo("old title")
        assertThat(resultTitleState).isEqualTo("new title")
    }

    @Test
    fun `enteredTitle - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val initialTitleState = getCurrentAdRecipeState().title

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle(""))
        val resultTitleState = getCurrentAdRecipeState().title

        verifyMocks()
        assertThat(initialTitleState).isEqualTo("title")
        assertThat(resultTitleState).isEqualTo("")
    }

    @Test
    fun `enteredDescription - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialDescriptionState = getCurrentAdRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val resultDescriptionState = getCurrentAdRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEmpty()
        assertThat(resultDescriptionState).isEqualTo("description")
    }

    @Test
    fun `enteredDescription - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("old description"))
        val initialDescriptionState = getCurrentAdRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("new description"))
        val resultDescriptionState = getCurrentAdRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEqualTo("old description")
        assertThat(resultDescriptionState).isEqualTo("new description")
    }

    @Test
    fun `enteredDescription - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialDescriptionState = getCurrentAdRecipeState().description

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription(""))
        val resultDescriptionState = getCurrentAdRecipeState().description

        verifyMocks()
        assertThat(initialDescriptionState).isEqualTo("description")
        assertThat(resultDescriptionState).isEqualTo("")
    }

    @Test
    fun `enteredIngredient - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialIngredientState = getCurrentAdRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("ingredient"))
        val resultIngredientState = getCurrentAdRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEmpty()
        assertThat(resultIngredientState).isEqualTo("ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - changed string`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("old ingredient"))
        val initialIngredientState = getCurrentAdRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("new ingredient"))
        val resultIngredientState = getCurrentAdRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("old ingredient")
        assertThat(resultIngredientState).isEqualTo("new ingredient")
    }

    @Test
    fun `enteredIngredient - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient("ingredient"))
        val initialIngredientState = getCurrentAdRecipeState().ingredient

        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredIngredient(""))
        val resultIngredientState = getCurrentAdRecipeState().ingredient

        verifyMocks()
        assertThat(initialIngredientState).isEqualTo("ingredient")
        assertThat(resultIngredientState).isEqualTo("")
    }

    @Test
    fun `onAddRecipe - title is empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAdRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAdRecipeState().titleError

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
        val initialTitleErrorState = getCurrentAdRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAdRecipeState().titleError

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
        val initialTitleErrorState = getCurrentAdRecipeState().titleError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAdRecipeState().titleError

        verifyMocks()
        assertThat(initialTitleErrorState).isNull()
        assertThat(resultTitleErrorState).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun `onAddRecipe - description is empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        val initialDescriptionErrorState = getCurrentAdRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAdRecipeState().descriptionError

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
        val initialDescriptionErrorState = getCurrentAdRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAdRecipeState().descriptionError

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
        val initialDescriptionErrorState = getCurrentAdRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultDescriptionErrorState = getCurrentAdRecipeState().descriptionError

        verifyMocks()
        assertThat(initialDescriptionErrorState).isNull()
        assertThat(resultDescriptionErrorState).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun `onAddRecipe - all fields are correct`() {
        val result = Resource.Success(true)

        setMocks()
        coEvery { addRecipeUseCase(any()) } returns flowOf(result)

        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredTitle("title"))
        addRecipeViewModel.onEvent(AddRecipeEvent.EnteredDescription("description"))
        val initialTitleErrorState = getCurrentAdRecipeState().titleError
        val initialDescriptionErrorState = getCurrentAdRecipeState().descriptionError

        addRecipeViewModel.onEvent(AddRecipeEvent.OnAddRecipe)
        val resultTitleErrorState = getCurrentAdRecipeState().titleError
        val resultDescriptionErrorState = getCurrentAdRecipeState().descriptionError

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
        val result = getCurrentAdRecipeState().ingredients
        val isLoading = getCurrentAdRecipeState().isLoading

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
        val result = getCurrentAdRecipeState().ingredients
        val isLoading = getCurrentAdRecipeState().isLoading

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
        val result = getCurrentAdRecipeState().ingredients
        val isLoading = getCurrentAdRecipeState().isLoading

        coVerify(exactly = 1) { getIngredientsUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `getCategories runs successfully`() {
        setMocks()
        coExcludeRecords { getIngredientsUseCase() }

        addRecipeViewModel = setViewModel()
        val result = getCurrentAdRecipeState().categories
        val isLoading = getCurrentAdRecipeState().isLoading

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
        val result = getCurrentAdRecipeState().categories
        val isLoading = getCurrentAdRecipeState().isLoading

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
        val result = getCurrentAdRecipeState().categories
        val isLoading = getCurrentAdRecipeState().isLoading

        coVerify(exactly = 1) { getCategoriesUseCase() }
        assertThat(result).isEmpty()
        assertThat(isLoading).isTrue()
    }

    @Test
    fun `onServingsButtonClicked - state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val resultServingsSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsSheetState).isFalse()
        assertThat(resultServingsSheetState).isTrue()
    }

    @Test
    fun `selectedServings - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsState = getCurrentAdRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val resultServingsState = getCurrentAdRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(0)
        assertThat(resultServingsState).isEqualTo(2)
    }

    @Test
    fun `selectedServings - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAdRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(5))
        val resultServingsState = getCurrentAdRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(5)
    }

    @Test
    fun `selectedServings - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAdRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(0))
        val resultServingsState = getCurrentAdRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerSaved - isServingsBottomSheetOpened state is set correctly`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val initialServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsBottomSheetState).isTrue()
        assertThat(resultServingsBottomSheetState).isFalse()
    }

    @Test
    fun `onServingsPickerSaved - servings are not selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        verifyMocks()
        assertThat(initialLastSavedServingsState).isEqualTo(0)
        assertThat(resultLastSavedServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerSaved - servings are selected`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(3))
        val initialLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        verifyMocks()
        assertThat(initialLastSavedServingsState).isEqualTo(0)
        assertThat(resultLastSavedServingsState).isEqualTo(3)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialServingsState = getCurrentAdRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAdRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(0)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 but selectedServings are not 0 - reset to default`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedServings(2))
        val initialServingsState = getCurrentAdRecipeState().selectedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAdRecipeState().selectedServings

        verifyMocks()
        assertThat(initialServingsState).isEqualTo(2)
        assertThat(resultServingsState).isEqualTo(0)
    }

    @Test
    fun `onServingsPickerDismissed - lastSavedServings is 0 - bottom sheet is closed`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked)
        val initialServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

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
        val initialServingsState = getCurrentAdRecipeState().selectedServings
        val initialLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAdRecipeState().selectedServings

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
        val initialServingsState = getCurrentAdRecipeState().selectedServings
        val initialLastSavedServingsState = getCurrentAdRecipeState().lastSavedServings

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed)
        val resultServingsState = getCurrentAdRecipeState().selectedServings

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
        val initialServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        addRecipeViewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved)
        val resultServingsBottomSheetState = getCurrentAdRecipeState().isServingsBottomSheetOpened

        verifyMocks()
        assertThat(initialServingsBottomSheetState).isTrue()
        assertThat(resultServingsBottomSheetState).isFalse()
    }

    @Test
    fun `selectedPrepTimeHours - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val resultPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("")
        assertThat(resultPrepTimeHoursState).isEqualTo("2 hours")
    }

    @Test
    fun `selectedPrepTimeHours - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val initialPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("3 hours"))
        val resultPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("2 hours")
        assertThat(resultPrepTimeHoursState).isEqualTo("3 hours")
    }

    @Test
    fun `selectedPrepTimeHours - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours("2 hours"))
        val initialPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours(""))
        val resultPrepTimeHoursState = getCurrentAdRecipeState().selectedPrepTimeHours

        verifyMocks()
        assertThat(initialPrepTimeHoursState).isEqualTo("2 hours")
        assertThat(resultPrepTimeHoursState).isEqualTo("")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        val initialPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val resultPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("")
        assertThat(resultPrepTimeMinutesState).isEqualTo("15 min")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially not empty - changed value`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val initialPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("35 min"))
        val resultPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("15 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("35 min")
    }

    @Test
    fun `selectedPrepTimeMinutes - initially not empty - result empty`() {
        setMocks()
        addRecipeViewModel = setViewModel()
        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes("15 min"))
        val initialPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        addRecipeViewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes(""))
        val resultPrepTimeMinutesState = getCurrentAdRecipeState().selectedPrepTimeMinutes

        verifyMocks()
        assertThat(initialPrepTimeMinutesState).isEqualTo("15 min")
        assertThat(resultPrepTimeMinutesState).isEqualTo("")
    }
}