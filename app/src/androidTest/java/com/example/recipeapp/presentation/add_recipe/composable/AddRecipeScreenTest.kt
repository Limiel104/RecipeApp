package com.example.recipeapp.presentation.add_recipe.composable

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageOptions
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

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        recipeIngredients = mapOf(
            Pair(
                Ingredient(
                    ingredientId = "ingredientId",
                    name = "Ingredient Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "200.0 g"
            ),
            Pair(
                Ingredient(
                    ingredientId = "ingredient2Id",
                    name = "Ingredient2 Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "5.0 kg"
            ),
            Pair(
                Ingredient(
                    ingredientId = "ingredient3Id",
                    name = "Ingredient3 Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "1 cup"
            ),
            Pair(
                Ingredient(
                    ingredientId = "ingredient4Id",
                    name = "Ingredient4 Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "125.0 ml"
            ),
            Pair(
                Ingredient(
                    ingredientId = "ingredient5Id",
                    name = "Ingredient5 Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "4 tbsp"
            ),
            Pair(
                Ingredient(
                    ingredientId = "ingredient6Id",
                    name = "Ingredient6 Name",
                    imageUrl = "imageUrl",
                    category = "category"
                ),
                "1 glass"
            )
        )
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { AddRecipeScreen(navController = navController) }
        }
    }

    private fun addRecipeState(
        recipeId: String = "",
        title: String = "",
        titleError: String? = null,
        prepTime: String = "",
        servings: Int = 0,
        description: String = "",
        descriptionError: String? = null,
        isVegetarian: Boolean = false,
        isVegan: Boolean = false,
        imageUrl: String = "",
        createdBy: String = "",
        categories: Map<Category, Boolean> = emptyMap(),
        ingredient: String = "",
        selectedServings: Int = 0,
        isServingsBottomSheetOpened: Boolean = false,
        lastSavedServings: Int = 0,
        selectedPrepTimeHours: String = "",
        selectedPrepTimeMinutes: String = "",
        isPrepTimeBottomSheetOpened: Boolean = false,
        lastSavedPrepTime: String = "",
        lastSavedPrepTimeHours: String = "",
        lastSavedPrepMinutes: String = "",
        ingredients: List<Ingredient> = emptyList(),
        isDropDownMenuExpanded: Boolean = false,
        isLoading: Boolean = false,
        recipeIngredients: Map<Ingredient, Quantity> = emptyMap(),
        isImageBottomSheetOpened: Boolean = false,
        imageUri: Uri? = Uri.EMPTY,
        tempUri: Uri? = Uri.EMPTY,
        cropImageOptions: CropImageOptions = CropImageOptions(
            maxCropResultWidth = 2400,
            maxCropResultHeight = 1800,
        ),
        dragIndex: String = "",
        dropIndex: String = "",
        draggedIngredientId: String = "",
        allIngredients: List<Ingredient> = emptyList(),
        isReorderModeActivated: Boolean = false,
        isQuantityBottomSheetOpened: Boolean = false,
        selectedWholeQuantity: String = "",
        selectedDecimalQuantity: String = "",
        selectedTypeQuantity: String = "",
        selectedIngredientId: String = "",
        index: Int = -1,
        isCategoriesDialogActivated: Boolean = false,
        lastSavedCategories: Map<Category, Boolean> = emptyMap()
    ): AddRecipeState {
        return AddRecipeState(
            recipeId = recipeId,
            title = title,
            titleError = titleError,
            prepTime = prepTime,
            servings = servings,
            description = description,
            descriptionError = descriptionError,
            isVegetarian = isVegetarian,
            isVegan = isVegan,
            imageUrl = imageUrl,
            createdBy = createdBy,
            categories = categories,
            ingredient = ingredient,
            selectedServings = selectedServings,
            isServingsBottomSheetOpened = isServingsBottomSheetOpened,
            lastSavedServings = lastSavedServings,
            selectedPrepTimeHours = selectedPrepTimeHours,
            selectedPrepTimeMinutes = selectedPrepTimeMinutes,
            isPrepTimeBottomSheetOpened = isPrepTimeBottomSheetOpened,
            lastSavedPrepTime = lastSavedPrepTime,
            lastSavedPrepTimeHours = lastSavedPrepTimeHours,
            lastSavedPrepMinutes = lastSavedPrepMinutes,
            ingredients = ingredients,
            isDropDownMenuExpanded = isDropDownMenuExpanded,
            isLoading = isLoading,
            recipeIngredients = recipeIngredients,
            isImageBottomSheetOpened = isImageBottomSheetOpened,
            imageUri = imageUri,
            tempUri = tempUri,
            cropImageOptions = cropImageOptions,
            dragIndex = dragIndex,
            dropIndex = dropIndex,
            draggedIngredientId = draggedIngredientId,
            allIngredients = allIngredients,
            isReorderModeActivated = isReorderModeActivated,
            isQuantityBottomSheetOpened = isQuantityBottomSheetOpened,
            selectedWholeQuantity = selectedWholeQuantity,
            selectedDecimalQuantity = selectedDecimalQuantity,
            selectedTypeQuantity = selectedTypeQuantity,
            selectedIngredientId = selectedIngredientId,
            index = index,
            isCategoriesDialogActivated = isCategoriesDialogActivated,
            lastSavedCategories = lastSavedCategories
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setScreenState(
        uiState: AddRecipeState = addRecipeState()
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
                onExpandedChange = {},
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
                onAddRecipe = {}
            )
        }
    }
}

    @Test
    fun addRecipeScreen_wholeLayoutSwipesVertically() {
        setScreenState(addRecipeState(recipeIngredients = recipeIngredients))

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
        autoCompleteIsNotDisplayed()
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
        descriptionTFIsNotDisplayed()
        ingredientsTextIsDisplayed()
        tapOrSwipeTextIsDisplayed()
        reorderButtonIsDisplayed()
        recipeIngredientItemsAreDisplayed()
        autoCompleteIsDisplayed()
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
        autoCompleteIsNotDisplayed()
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
        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeUp() }

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)

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
        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeUp() }

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)

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
        composeRule
            .onNodeWithTag("Add Recipe Content")
            .performTouchInput { swipeUp() }

        val initialTitleNode = composeRule.onNodeWithTag("Add recipe title TF").fetchSemanticsNode()
        val initialErrorValue = initialTitleNode.config.getOrNull(SemanticsProperties.Error)

        composeRule.onNodeWithTag("Add recipe title TF").performTextInput(title)
        composeRule.onNodeWithTag("Add recipe description TF").performTextInput(description)

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

        checkAddRecipeButton()
        val resultDescriptionNode = composeRule.onNodeWithTag("Add recipe description TF").fetchSemanticsNode()
        val errorLabel = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        val resultErrorValue = resultDescriptionNode.config.getOrNull(SemanticsProperties.Text)?.get(1).toString()

        assertThat(initialErrorValue).isNull()
        assertThat(errorLabel).isEqualTo("Description")
        assertThat(resultErrorValue).isEqualTo("At least one character is not allowed")
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