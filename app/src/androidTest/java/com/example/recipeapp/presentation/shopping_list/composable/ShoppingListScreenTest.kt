package com.example.recipeapp.presentation.shopping_list.composable

import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.di.AppModule
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Quantity
import com.example.recipeapp.presentation.MainActivity
import com.example.recipeapp.presentation.shopping_list.ShoppingListState
import com.example.recipeapp.ui.theme.RecipeAppTheme
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ShoppingListScreenTest {

    private lateinit var shoppingListIngredients: List<Ingredient>
    private lateinit var emptyShoppingListIngredients: Map<Ingredient, Quantity>
    private lateinit var shoppingListIngredientsWithOneItem: List<Ingredient>
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
                category = "category3"
            ),
            Ingredient(
                ingredientId = "ingredient3Id",
                name = "Ingredient3 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient4Id",
                name = "Ingredient4 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient5Id",
                name = "Ingredient5 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient6Id",
                name = "Ingredient6 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient7Id",
                name = "Ingredient7 Name",
                imageUrl = "imageUrl",
                category = "category"
            ),
            Ingredient(
                ingredientId = "ingredient8Id",
                name = "Ingredient8 Name",
                imageUrl = "imageUrl",
                category = "category3"
            ),
            Ingredient(
                ingredientId = "ingredient9Id",
                name = "Ingredient9 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient10Id",
                name = "Ingredient10 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient11Id",
                name = "Ingredient11 Name",
                imageUrl = "imageUrl",
                category = "category3"
            ),
            Ingredient(
                ingredientId = "ingredient12Id",
                name = "Ingredient12 Name",
                imageUrl = "imageUrl",
                category = "category2"
            ),
            Ingredient(
                ingredientId = "ingredient13Id",
                name = "Ingredient13 Name",
                imageUrl = "imageUrl",
                category = "category2"
            )

        )

        shoppingListIngredients = listOf(
            allIngredients[1],
            allIngredients[3],
            allIngredients[4],
            allIngredients[8],
            allIngredients[0],
            allIngredients[12],
            allIngredients[5],
            allIngredients[9],
            allIngredients[11],
            allIngredients[2]
        )

        emptyShoppingListIngredients = emptyMap<Ingredient, Quantity>()

        shoppingListIngredientsWithOneItem = listOf(allIngredients[1])
    }

    private fun setScreen() {
        composeRule.activity.setContent {
            val navController = rememberNavController()
            RecipeAppTheme() { ShoppingListScreen(
                navController = navController,
                viewModel = hiltViewModel()
            ) }
        }    
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    private fun setScreenState(
        uiState: ShoppingListState = ShoppingListState()
    ) {
        composeRule.activity.setContent {
            RecipeAppTheme {
                ShoppingListContent(
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                        rememberTopAppBarState()
                    ),
                    modalBottomSheetState = rememberModalBottomSheetState(),
                    uiState = uiState,
                    onIngredientSuggestionClick = {},
                    onDropDownMenuExpandedChange = {},
                    onIngredientChange = {},
                    onAddIngredientsDialogDismiss = {},
                    onAddIngredientsSave = {},
                    onAddButtonClick = {},
                    onIngredientClick = {},
                    onSelectedWholeQuantity = {},
                    onSelectedDecimalQuantity = {},
                    onSelectedTypeQuantity = {},
                    onQuantityPickerDismiss = {},
                    onQuantityPickerSave = {},
                    onCheckedChange = {},
                    onMenuButtonClicked = {},
                    onMenuDismissed = {},
                    onNameChange = {},
                    onOpenRenameShoppingListDialog = {},
                    onRenameShoppingListDialogSaved = {},
                    onRenameShoppingListDialogDismissed = {},
                    onDeleteAllIngredients = {},
                    onDeleteShoppingList = {},
                    onAddNewShoppingList = {},
                    onSelectedShoppingList = {},
                    onOpenOtherShoppingListsMenu = {},
                    onOtherShoppingListsMenuDismiss = {},
                    onSwipeToDelete = {}
                )
            }
        }
    }
    
    @Test
    fun savedRecipesScreen_wholeLayoutSwipesVertically() {
        setScreenState(
            ShoppingListState(
                shoppingListName = "Shopping List Name",
                shoppingListIngredients = shoppingListIngredients.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
            )
        )

        menuButtonIsDisplayed()
        nameIsDisplayed()
        itemsNumberTextIsDisplayed()
        isCategoryItemNotDisplayed()
        isCategory2ItemDisplayed()
        isCategory3ItemDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Shopping List Content")
            .performTouchInput { swipeUp() }

        menuButtonIsDisplayed()
        nameIsDisplayed()
        itemsNumberTextIsNotDisplayed()
        isCategoryItemDisplayed()
        isCategory2ItemDisplayed()
        isCategory3ItemNotDisplayed()
        addButtonIsDisplayed()

        composeRule
            .onNodeWithTag("Shopping List Content")
            .performTouchInput { swipeDown() }

        menuButtonIsDisplayed()
        nameIsDisplayed()
        itemsNumberTextIsDisplayed()
        isCategoryItemNotDisplayed()
        isCategory2ItemDisplayed()
        isCategory3ItemDisplayed()
        addButtonIsDisplayed()
    }

    @Test
    fun nameText_nameOfShoppingListIsDisplayedCorrectly_ShoppingListNameIsLong() {
        setScreenState(
            ShoppingListState(shoppingListName = "This is very long Shopping List Name.")
        )

        composeRule.onNode(hasText("This is very long Shopping List Name.")).assertIsDisplayed()
        composeRule.onNode(hasText("This is very long Shopping List Name.")).onParent().assertHeightIsEqualTo(152.dp)
    }

    @Test
    fun nameText_nameOfShoppingListIsDisplayedCorrectly_ShoppingListNameIsShort() {
        setScreenState(ShoppingListState(shoppingListName = "List"))

        composeRule.onNode(hasText("List")).assertIsDisplayed()
        composeRule.onNode(hasText("List")).onParent().assertHeightIsEqualTo(152.dp)
    }

    @Test
    fun itemsText_numberOfItemsIsDisplayedCorrectly_ShoppingListIsEmpty() {
        setScreenState(
            ShoppingListState(shoppingListIngredients = emptyShoppingListIngredients)
        )

        composeRule.onNode(hasText(
            text = "items",
            substring = true
        )).assert(hasText("0 items"))
    }

    @Test
    fun itemsText_numberOfItemsIsDisplayedCorrectly_onlyOneItem() {
        setScreenState(
            ShoppingListState(shoppingListIngredients = shoppingListIngredientsWithOneItem.associateWith { "" })
        )

        composeRule.onNode(hasText(
            text = "item",
            substring = true
        )).assert(hasText("1 item"))
    }

    @Test
    fun itemsText_numberOfItemsIsDisplayedCorrectly_moreThanOneItem() {
        setScreenState(
            ShoppingListState(shoppingListIngredients = allIngredients.associateWith { "" })
        )

        composeRule.onNode(hasText(
            text = "items",
            substring = true
        )).assert(hasText("13 items"))
    }

    @Test
    fun categoryItem_categoryNameIsDisplayedCorrectly() {
        setScreenState(
            ShoppingListState(
                shoppingListIngredients = shoppingListIngredientsWithOneItem.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
            ))

        val categoryName = composeRule.onNodeWithTag("Shopping List Category category3")
            .fetchSemanticsNode()
            .children[0].config
            .getOrElse(SemanticsProperties.Text) { emptyList() }
            .first()
            .text

        Truth.assertThat(categoryName).isEqualTo("category3")
    }

    @Test
    fun categoryItem_numberOfIngredientsIsDisplayedCorrectly_onlyOneItem() {
        setScreenState(
            ShoppingListState(
                shoppingListIngredients = shoppingListIngredientsWithOneItem.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
        ))

        var numberOfIngredients = 0
        val children = composeRule.onNodeWithTag("category3 column")
            .fetchSemanticsNode()
            .children

        for(child in children) {
            val testTag = child.config.getOrElse(SemanticsProperties.TestTag) { "" }
            if(testTag.contains("Ingredient Item"))
                numberOfIngredients += 1
        }

        Truth.assertThat(numberOfIngredients).isEqualTo(1)
    }

    @Test
    fun categoryItem_ingredientsImageIsDisplayedCorrectly_onlyOneItem() {
        setScreenState(
            ShoppingListState(
                shoppingListIngredients = shoppingListIngredientsWithOneItem.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
            ))

        var numberOfImages = 0
        val children = composeRule.onNodeWithTag("category3 column")
            .fetchSemanticsNode()
            .children

        for(child in children) {
            println("AAAAAA ${child.config}")
            val role = child.config.getOrElse(SemanticsProperties.Role) { Role.RadioButton }
            if(role == Role.Image)
                numberOfImages += 1
        }

        Truth.assertThat(numberOfImages).isEqualTo(1)
    }

    @Test
    fun categoryItem_numberOfIngredientsIsDisplayedCorrectly_moreThanOneItem() {
        setScreenState(
            ShoppingListState(
                shoppingListIngredients = allIngredients.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
            ))

        var numberOfIngredients = 0
        val children = composeRule.onNodeWithTag("category3 column")
            .fetchSemanticsNode()
            .children

        for(child in children) {
            val testTag = child.config.getOrElse(SemanticsProperties.TestTag) { "" }
            if(testTag.contains("Ingredient Item"))
                numberOfIngredients += 1
        }

        Truth.assertThat(numberOfIngredients).isEqualTo(3)
    }

    @Test
    fun categoryItem_ingredientsImageIsDisplayedCorrectly_moreThanOneItem() {
        setScreenState(
            ShoppingListState(
                shoppingListIngredients = allIngredients.associateWith { "" },
                checkedIngredients = allIngredients.associateWith { false }
            ))

        var numberOfImages = 0
        val children = composeRule.onNodeWithTag("category3 column")
            .fetchSemanticsNode()
            .children

        for(child in children) {
            val role = child.config.getOrElse(SemanticsProperties.Role) { Role.RadioButton }
            if(role == Role.Image)
                numberOfImages += 1
        }

        Truth.assertThat(numberOfImages).isEqualTo(3)
    }

    private fun menuButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Menu button")
        .assertIsDisplayed()

    private fun nameIsDisplayed() = composeRule
        .onNode(hasText("Shopping List Name"))
        .assertIsDisplayed()

    private fun itemsNumberTextIsDisplayed() = composeRule
        .onNode(hasText(
            text = "items",
            substring = true
        ))
        .assertIsDisplayed()

    private fun itemsNumberTextIsNotDisplayed() = composeRule
        .onNode(hasText(
            text = "items",
            substring = true
        ))
        .assertIsNotDisplayed()

    private fun isCategoryItemDisplayed() = composeRule
        .onNodeWithTag("Shopping List Category category")
        .assertIsDisplayed()

    private fun isCategoryItemNotDisplayed() = composeRule
        .onNodeWithTag("Shopping List Category category")
        .assertIsNotDisplayed()

    private fun isCategory2ItemDisplayed() = composeRule
        .onNodeWithTag("Shopping List Category category2")
        .assertIsDisplayed()

    private fun isCategory3ItemDisplayed() = composeRule
        .onNodeWithTag("Shopping List Category category3")
        .assertIsDisplayed()

    private fun isCategory3ItemNotDisplayed() = composeRule
        .onNodeWithTag("Shopping List Category category3")
        .assertIsNotDisplayed()

    private fun addButtonIsDisplayed() = composeRule
        .onNodeWithContentDescription("Add button")
        .assertIsDisplayed()
}