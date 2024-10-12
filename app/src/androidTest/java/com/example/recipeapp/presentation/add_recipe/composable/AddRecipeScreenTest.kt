package com.example.recipeapp.presentation.add_recipe.composable

import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.add_recipe.AddRecipeState
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AddRecipeScreenTest {

    private lateinit var recipeIngredients: Map<Ingredient, Quantity>
    private lateinit var categories: Map<Category, Boolean>
    private lateinit var categoryIds: List<String>
    private lateinit var ingredients: List<Ingredient>
    private lateinit var allIngredients: List<Ingredient>

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        allIngredients = listOf(
            Ingredient(
                ingredientId = "ingredientId",
                name = "Ingredient Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient2Id",
                name = "Ingredient2 Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient3 Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient4Id",
                name = "Ingredient4 Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient5Id",
                name = "Ingredient5 Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient6Id",
                name = "Ingredient6 Name",
                imageUrl = "imageUrl",
                category = "category"
            )
        )

        ingredients = listOf(
            allIngredients[1],
            allIngredients[3],
            allIngredients[4]
        )

        recipeIngredients = mapOf(
            Pair(allIngredients[0], "200.0 g"),
            Pair(allIngredients[1], "5.0 kg"),
            Pair(allIngredients[2], "1 cup"),
            Pair(allIngredients[3], "125.0 ml"),
            Pair(allIngredients[4], "4 tbsp"),
            Pair(allIngredients[5], "1 glass")
        )

        categoryIds = listOf(
            "Appetizer", "Breakfast",
            "Beef", "Chicken", "Dessert",
            "Dinner", "Fish", "Pasta",
            "Pork", "Pizza", "Salad",
            "Salmon", "Soup", "Stew"
        )

        val categoriesMap = mutableMapOf<Category, Boolean>()
        for(category in categoryIds) {
            categoriesMap[Category(category, "")] = false
        }
        categories = categoriesMap
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { AddRecipeScreen(navController = navController) }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setScreenState(
        uiState: AddRecipeState = AddRecipeState()
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme() {
                AddRecipeContent(
                    scrollState = rememberScrollState(),
                    modalBottomSheetState = rememberModalBottomSheetState(),
                    uiState = uiState,
                    onTitleChange = {},
                    onDescriptionChange = {},
                    onIngredientChange = {},
                    onSelectedServings = {},
                    onServingsPickerDismiss = {},
                    onServingsPickerSave = {},
                    onServingsButtonClicked = {},
                    onSelectedPrepTimeHours = {},
                    onSelectedPrepTimeMinutes = {},
                    onPrepTimePickerDismiss = {},
                    onPrepTimePickerSave = {},
                    onPrepTimeButtonClicked = {},
                    onDropDownMenuExpandedChange = {},
                    onIngredientSuggestionClick = {},
                    onAddPhoto = {},
                    onTakePhoto = {},
                    onSelectImage = {},
                    onAddImageDismiss = {},
                    onReorder = {},
                    onDragIndexChange = {},
                    onDropIndexChange = {},
                    onDraggedIngredientChange = {},
                    onSwipeToDelete = {},
                    onIngredientClicked = {},
                    onSelectedWholeQuantity = {},
                    onSelectedDecimalQuantity = {},
                    onSelectedTypeQuantity = {},
                    onQuantityPickerDismiss = {},
                    onQuantityPickerSave = {},
                    onCategoriesButtonClicked = {},
                    onCheckBoxToggled = {},
                    onDialogDismiss = {},
                    onDialogSave = {},
                    onGoBack = {},
                    onAddRecipe = {}
                )
            }
        }
    }

    @Test
    fun addRecipeScreen_wholeLayoutSwipesVertically() {
        setScreenState(AddRecipeState(recipeIngredients = recipeIngredients))

        backButtonIsDisplayed()
        titleIsDisplayed()
        addButtonIsDisplayed()
        titleTextIsDisplayed()
        titleTFIsDisplayed()
        addImageIsDisplayed()
        descriptionTextIsDisplayed()
        descriptionTFIsDisplayed()
        ingredientsTextIsDisplayed()
        tapOrSwipeTextIsDisplayed()
        reorderButtonIsDisplayed()
        recipeIngredientItemsAreDisplayed()
        assertThat(autoCompleteIsNotDisplayed()).isTrue()
        servingsSectionIsNotDisplayed()
        prepTimeSectionIsNotDisplayed()
        categoriesSectionIsNotDisplayed()

        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeUp() }

        backButtonIsDisplayed()
        titleIsDisplayed()
        addButtonIsDisplayed()
        titleTextIsNotDisplayed()
        titleTFIsNotDisplayed()
        addImageIsNotDisplayed()
        descriptionTextIsNotDisplayed()
        descriptionTFIsDisplayed()
        ingredientsTextIsDisplayed()
        tapOrSwipeTextIsDisplayed()
        reorderButtonIsDisplayed()
        recipeIngredientItemsAreDisplayed()
        assertThat(autoCompleteIsNotDisplayed()).isTrue()
        servingsSectionIsDisplayed()
        prepTimeSectionIsDisplayed()
        categoriesSectionIsDisplayed()

        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeDown() }

        backButtonIsDisplayed()
        titleIsDisplayed()
        addButtonIsDisplayed()
        titleTextIsDisplayed()
        titleTFIsDisplayed()
        addImageIsDisplayed()
        descriptionTextIsDisplayed()
        descriptionTFIsDisplayed()
        ingredientsTextIsDisplayed()
        tapOrSwipeTextIsDisplayed()
        reorderButtonIsDisplayed()
        recipeIngredientItemsAreDisplayed()
        assertThat(autoCompleteIsNotDisplayed()).isTrue()
        servingsSectionIsNotDisplayed()
        prepTimeSectionIsNotDisplayed()
        categoriesSectionIsNotDisplayed()
    }

    @Test
    fun titleTextField_inputTextIsDisplayedCorrectly() {
        val title = "Recipe Title"
        setScreenState(AddRecipeState(title = title))

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        val titleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val textInput = titleNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(title)
    }

    @Test
    fun descriptionTextField_inputTextIsDisplayedCorrectly() {
        val description = "Recipe Description"
        setScreenState(AddRecipeState(description = description))

        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        val descriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val textInput = descriptionNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(description)
    }

    @Test
    fun titleErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(AddRecipeState(titleError = "Field can't be empty"))

        val titleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val errorLabel = titleNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = titleNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Title")
        assertThat(errorValue).isEqualTo("Field can't be empty")
    }

    @Test
    fun descriptionErrorTextField_errorIsDisplayedCorrectly() {
        setScreenState(AddRecipeState(descriptionError = "Field can't be empty"))

        val descriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val errorLabel = descriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val errorValue = descriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()
        assertThat(errorLabel).isEqualTo("Description")
        assertThat(errorValue).isEqualTo("Field can't be empty")
    }

    @Test
    fun titleErrorTextField_performAddRecipeWhileTitleTextFieldIsEmpty_errorDisplayedCorrectly() {
        val description = "Recipe Description"
        setScreen()

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        Espresso.closeSoftKeyboard()

        checkAddRecipeButton()
        val resultTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val errorLabel = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Title")
        assertThat(resultErrorValue).isEqualTo("Field can't be empty")
    }

    @Test
    fun descriptionErrorTextField_performAddRecipeWhileDescriptionTextFieldIsEmpty_errorDisplayedCorrectly() {
        val title = "Recipe Title"
        setScreen()

        val initialDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val initialErrorValue = initialDescriptionNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)

        checkAddRecipeButton()
        val resultDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val errorLabel = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Description")
        assertThat(resultErrorValue).isEqualTo("Field can't be empty")
    }

    @Test
    fun titleErrorTextField_performAddRecipeWhileTitleTextFieldIsTooShort_errorDisplayedCorrectly() {
        val title = "Rec"
        val description = "Recipe Description"
        setScreen()

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        Espresso.closeSoftKeyboard()

        checkAddRecipeButton()
        val resultTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val errorLabel = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Title")
        assertThat(resultErrorValue).isEqualTo("Field is too short")
    }

    @Test
    fun descriptionErrorTextField_performAddRecipeWhileDescriptionTextFieldIsTooShort_errorDisplayedCorrectly() {
        val title = "Recipe Title"
        val description = "Rec"
        setScreen()

        val initialDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val initialErrorValue = initialDescriptionNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        Espresso.closeSoftKeyboard()

        checkAddRecipeButton()
        val resultDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val errorLabel = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Description")
        assertThat(resultErrorValue).isEqualTo("Field is too short")
    }

    @Test
    fun titleErrorTextField_performAddRecipeWhileTitleTextFieldContainsSpecialChars_errorDisplayedCorrectly() {
        val title = "Recipe_Title"
        val description = "Recipe Description"
        setScreen()

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        Espresso.closeSoftKeyboard()

        checkAddRecipeButton()
        val resultTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val errorLabel = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultTitleNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Title")
        assertThat(resultErrorValue).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun descriptionErrorTextField_performAddRecipeWhileDescriptionTextFieldContainsSpecialChars_errorDisplayedCorrectly() {
        val title = "Recipe Title"
        val description = "Recipe_Description"
        setScreen()

        val initialDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val initialErrorValue = initialDescriptionNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)
        Espresso.closeSoftKeyboard()

        checkAddRecipeButton()
        val resultDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val errorLabel = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Description")
        assertThat(resultErrorValue).isEqualTo("At least one character is not allowed")
    }

    @Test
    fun servingsPicker_isDisplayedCorrectly() {
        setScreenState(AddRecipeState(isServingsBottomSheetOpened = true))

        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()
        composeRule.onNodeWithTag("Servings picker").onChildAt(0).assert(hasText("Servings"))
        composeRule.onNodeWithText("Select number of portions this recipe makes").assertIsDisplayed()
        composeRule.onAllNodesWithTag("Servings number picker").onFirst().assertIsDisplayed()
        composeRule.onNodeWithTag("Save servings button").assertIsDisplayed()
        composeRule.onNodeWithTag("Save servings button").assertIsEnabled()
    }

    @Test
    fun servingsPicker_opensCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Servings button").performScrollTo()
        composeRule.onNodeWithTag("Servings picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Servings button").performClick()
        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()
    }

    @Test
    fun servingsPicker_closesCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Servings button").performScrollTo()
        composeRule.onNodeWithTag("Servings picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Servings button").performClick()
        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()

        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()
        composeRule.onNodeWithTag("Save servings button").performClick()
        composeRule.onNodeWithTag("Servings picker").assertIsNotDisplayed()
    }

    @Test
    fun servingsPicker_closesCorrectlyWhenPressedBackButton() {
        setScreen()

        composeRule.onNodeWithTag("Servings button").performScrollTo()
        composeRule.onNodeWithTag("Servings picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Servings button").performClick()
        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()

        composeRule.onNodeWithTag("Servings picker").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithTag("Servings picker").assertIsNotDisplayed()
    }

    @Test
    fun prepTimePicker_isDisplayedCorrectly() {
        setScreenState(AddRecipeState(isPrepTimeBottomSheetOpened = true))

        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()
        composeRule.onNodeWithTag("Prep time picker").onChildAt(0).assert(hasText("Prep time"))
        composeRule.onNodeWithText("How long does it take to prepare this recipe").assertIsDisplayed()
        composeRule.onAllNodesWithTag("Hours list item picker").onFirst().assertIsDisplayed()
        composeRule.onNodeWithTag("Save prep time button").assertIsDisplayed()
        composeRule.onNodeWithTag("Save prep time button").assertIsEnabled()
    }

    @Test
    fun prepTimePicker_opensCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Prep time button").performScrollTo()
        composeRule.onNodeWithTag("Prep time picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Prep time button").performClick()
        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()
    }

    @Test
    fun prepTimePicker_closesCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Prep time button").performScrollTo()
        composeRule.onNodeWithTag("Prep time picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Prep time button").performClick()
        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()

        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()
        composeRule.onNodeWithTag("Save prep time button").performClick()
        composeRule.onNodeWithTag("Prep time picker").assertIsNotDisplayed()
    }

    @Test
    fun prepTimePicker_closesCorrectlyWhenPressedBackButton() {
        setScreen()

        composeRule.onNodeWithTag("Prep time button").performScrollTo()
        composeRule.onNodeWithTag("Prep time picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Prep time button").performClick()
        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()

        composeRule.onNodeWithTag("Prep time picker").assertIsDisplayed()
        Espresso.pressBack()
        composeRule.onNodeWithTag("Prep time picker").assertIsNotDisplayed()
    }

    @Test
    fun categoriesDialog_isDisplayedCorrectly() {
        setScreenState(
            AddRecipeState(
                categories = categories,
                isCategoriesDialogActivated = true
            )
        )

        composeRule.onNodeWithContentDescription("Clear button").assertIsDisplayed()
        composeRule.onNodeWithText("Select recipe categories").assertIsDisplayed()
        composeRule.onNodeWithTag("Save button").assertIsDisplayed()
        composeRule.onNodeWithTag("Category lazy column").assertIsDisplayed()
    }

    @Test
    fun categoriesDialog_opensCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Categories button").performScrollTo()
        composeRule.onNodeWithTag("Categories dialog").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Categories button").performClick()
        composeRule.onNodeWithTag("Categories dialog").assertIsDisplayed()
    }

    @Test
    fun categoriesDialog_closesCorrectlyByClickingClearButton() {
        setScreen()

        composeRule.onNodeWithTag("Categories button").performScrollTo()
        composeRule.onNodeWithTag("Categories dialog").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Categories button").performClick()
        composeRule.onNodeWithTag("Categories dialog").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Clear button").performClick()
        composeRule.onNodeWithTag("Categories dialog").assertIsNotDisplayed()
    }

    @Test
    fun categoriesDialog_closesCorrectlyByClickingSaveButton() {
        setScreen()

        composeRule.onNodeWithTag("Categories button").performScrollTo()
        composeRule.onNodeWithTag("Categories dialog").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Categories button").performClick()
        composeRule.onNodeWithTag("Categories dialog").assertIsDisplayed()

        composeRule.onNodeWithTag("Save button").performClick()
        composeRule.onNodeWithTag("Categories dialog").assertIsNotDisplayed()
    }

    @Test
    fun categoriesDialog_lazyColumnScrollsVertically() {
        setScreenState(
            AddRecipeState(
                categories = categories,
                isCategoriesDialogActivated = true
            )
        )

        composeRule.onNodeWithTag("Category checkbox item Appetizer").assertIsDisplayed()
        composeRule.onNodeWithTag("Category checkbox item Stew").assertIsNotDisplayed()

        composeRule.onNodeWithTag("Category lazy column").performScrollToNode(
            hasTestTag("Category checkbox item Stew")
        )
        composeRule.onNodeWithTag("Category checkbox item Appetizer").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Category checkbox item Stew").assertIsDisplayed()

        composeRule.onNodeWithTag("Category lazy column").performScrollToNode(
            hasTestTag("Category checkbox item Appetizer"))
        composeRule.onNodeWithTag("Category checkbox item Appetizer").assertIsDisplayed()
        composeRule.onNodeWithTag("Category checkbox item Stew").assertIsNotDisplayed()
    }

    @Test
    fun addImageCard_isDisplayedCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Add image card").assertHeightIsEqualTo(150.dp)
        composeRule.onNodeWithTag("Add image card").assertHasClickAction()
        composeRule.onNodeWithContentDescription("Add image button").assertIsDisplayed()
        composeRule.onNodeWithText("Add image").assertIsDisplayed()
    }

    @Test
    fun imagePicker_isDisplayedCorrectly() {
        setScreenState(AddRecipeState(isImageBottomSheetOpened = true))

        composeRule.onNodeWithText("Take Photo").assertIsDisplayed()
        composeRule.onNodeWithText("Select Image").assertIsDisplayed()
    }

    @Test
    fun imagePicker_opensCorrectly() {
        setScreen()

        composeRule.onNodeWithTag("Image picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Add image card").performClick()
        composeRule.onNodeWithTag("Image picker").assertIsDisplayed()
    }

    @Test
    fun imagePicker_closesCorrectlyBySwipeDown() {
        setScreen()

        composeRule.onNodeWithTag("Image picker").assertIsNotDisplayed()
        composeRule.onNodeWithTag("Add image card").performClick()
        composeRule.onNodeWithTag("Image picker").assertIsDisplayed()
        composeRule.onNodeWithTag("Image picker").performTouchInput { swipeDown() }
        composeRule.onNodeWithTag("Image picker").assertIsNotDisplayed()
    }

    @Test
    fun autoComplete_inputTextIsDisplayedCorrectly() {
        val query = "Recipe Title"
        setScreenState(AddRecipeState(ingredient = query))

        composeRule.onNodeWithTag("Autocomplete TF").performTextInput(query)
        val autocompleteNode = composeRule.onNodeWithTag("Autocomplete TF").fetchSemanticsNode()
        val textInput = autocompleteNode.config.getOrNull(SemanticsProperties.EditableText).toString()
        assertThat(textInput).isEqualTo(query)
    }

    @Test
    fun autoComplete_isDisplayedCorrectly() {
        setScreenState(
            AddRecipeState(
                isDropDownMenuExpanded = true,
                allIngredients = allIngredients,
                ingredientsToSelect = ingredients,
            )
        )

        Thread.sleep(5000L)

        composeRule.onNodeWithTag("Autocomplete TF").isDisplayed()
        composeRule.onNodeWithTag("Autocomplete DDM")
        composeRule.onNodeWithText("Ingredient2 Name").isDisplayed()
        composeRule.onNodeWithText("Ingredient4 Name").isDisplayed()
        composeRule.onNodeWithText("Ingredient5 Name").isDisplayed()
        val leftIngredients = composeRule.onNodeWithTag("Autocomplete DDM").fetchSemanticsNode().children.size
        assertThat(leftIngredients).isEqualTo(3)
    }

    @Test
    fun autoComplete_isExtendedAfterClick() {
        setScreen()

        composeRule.onNodeWithTag("Autocomplete DDM").isNotDisplayed()
        composeRule.onNodeWithTag("Add ingredient name EDDM").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isDisplayed()
    }

    @Test
    fun autoComplete_isClosedAfterClickOnTF() {
        setScreen()

        composeRule.onNodeWithTag("Add ingredient name EDDM").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isDisplayed()
        composeRule.onNodeWithTag("Autocomplete TF").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isNotDisplayed()
    }

    @Test
    fun autoComplete_isClosedAfterClosingKeyboard() {
        setScreen()

        composeRule.onNodeWithTag("Add ingredient name EDDM").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isDisplayed()
        Espresso.closeSoftKeyboard()
        composeRule.onNodeWithTag("Autocomplete DDM").isNotDisplayed()
    }

    @Test
    fun autoComplete_isClosedAfterClickOnOutsideElement() {
        setScreen()

        composeRule.onNodeWithTag("Add ingredient name EDDM").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isDisplayed()
        composeRule.onNodeWithTag("Description").performClick()
        composeRule.onNodeWithTag("Autocomplete DDM").isNotDisplayed()
    }

    @Test
    fun recipeIngredientList_isDisplayedCorrectly() {
        setScreenState(AddRecipeState(recipeIngredients = recipeIngredients))

        ingredientsTextIsDisplayed()
        tapOrSwipeTextIsDisplayed()
        reorderButtonIsDisplayed()
        composeRule.onNodeWithTag("Add recipe ingredient list").isDisplayed()
        val ingredientCount = composeRule.onAllNodesWithTag("Recipe Ingredient Item").fetchSemanticsNodes().size
        assertThat(ingredientCount).isEqualTo(6)
    }

    @Test
    fun quantityPicker_isDisplayedCorrectly() {
        setScreenState(AddRecipeState(isQuantityBottomSheetOpened = true))

        composeRule.onNodeWithTag("Quantity picker").isDisplayed()
        composeRule.onAllNodesWithTag("Whole list item picker").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithTag("Decimal list item picker").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithTag("Type list item picker").onFirst().assertIsDisplayed()
        composeRule.onNodeWithTag("Save quantity button").isDisplayed()
    }

    private fun backButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Back button")
        .assertIsDisplayed()

    private fun titleIsDisplayed() = composeRule
        .onNodeWithText("Add recipe")
        .assertIsDisplayed()

    private fun titleIsNotDisplayed() = composeRule
        .onNodeWithText("Add recipe")
        .assertIsNotDisplayed()

    private fun addButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Add recipe button")
        .assertIsDisplayed()

    private fun addButtonIsNotDisplayed() = composeRule
        .onNodeWithContentDescription("Add recipe button")
        .assertIsNotDisplayed()

    private fun titleTextIsDisplayed() = composeRule
        .onNodeWithTag("Title")
        .assertIsDisplayed()

    private fun titleTextIsNotDisplayed() = composeRule
        .onNodeWithTag("Title")
        .assertIsNotDisplayed()

    private fun titleTFIsDisplayed() = composeRule
        .onNodeWithTag("Add recipe title TF")
        .assertIsDisplayed()

    private fun titleTFIsNotDisplayed() = composeRule
        .onNodeWithTag("Add recipe title TF")
        .assertIsNotDisplayed()

    private fun addImageIsDisplayed() = composeRule
        .onNodeWithTag("Add image card")
        .assertIsDisplayed()

    private fun addImageIsNotDisplayed() = composeRule
        .onNodeWithTag("Add image card")
        .assertIsNotDisplayed()

    private fun descriptionTextIsDisplayed() = composeRule
        .onNodeWithTag("Description")
        .assertIsDisplayed()

    private fun descriptionTextIsNotDisplayed() = composeRule
        .onNodeWithTag("Description")
        .assertIsNotDisplayed()

    private fun descriptionTFIsDisplayed() = composeRule
        .onNodeWithTag("Add recipe description TF")
        .assertIsDisplayed()

    private fun descriptionTFIsNotDisplayed() = composeRule
        .onNodeWithTag("Add recipe description TF")
        .assertIsNotDisplayed()

    private fun ingredientsTextIsDisplayed() = composeRule
        .onNodeWithText("Ingredients")
        .assertIsDisplayed()

    private fun tapOrSwipeTextIsDisplayed() = composeRule
        .onNodeWithText("Tap to edit, swipe to delete")
        .assertIsDisplayed()

    private fun reorderButtonIsDisplayed() = composeRule
        .onNodeWithTag("Reorder button")
        .assertIsDisplayed()

    private fun recipeIngredientItemsAreDisplayed() = composeRule
        .onAllNodesWithTag("Recipe Ingredient Item")
        .onFirst()
        .assertIsDisplayed()

    private fun autoCompleteIsDisplayed() = composeRule
        .onNodeWithText("Add recipe type ingredient name EDDM")
        .isDisplayed()

    private fun autoCompleteIsNotDisplayed() = composeRule
        .onNodeWithText("Add recipe type ingredient name EDDM")
        .isNotDisplayed()

    private fun servingsSectionIsDisplayed() = composeRule
        .onNodeWithTag("Servings")
        .assertIsDisplayed()

    private fun servingsSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Servings")
        .assertIsNotDisplayed()

    private fun prepTimeSectionIsDisplayed() = composeRule
        .onNodeWithTag("Prep time")
        .assertIsDisplayed()

    private fun prepTimeSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Prep time")
        .assertIsNotDisplayed()

    private fun categoriesSectionIsDisplayed() = composeRule
        .onNodeWithTag("Categories")
        .assertIsDisplayed()

    private fun categoriesSectionIsNotDisplayed() = composeRule
        .onNodeWithTag("Categories")
        .assertIsNotDisplayed()

    private fun checkAddRecipeButton() {
        composeRule.onNodeWithContentDescription("Add recipe button").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Add recipe button").assertIsEnabled()
        composeRule.onNodeWithContentDescription("Add recipe button").performClick()
    }
}