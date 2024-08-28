@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.presentation.add_recipe.AddRecipeState
import com.example.recipeapp.presentation.common.composable.AutoComplete
import com.example.recipeapp.presentation.common.composable.RecipeIngredientItem
import com.example.recipeapp.presentation.common.getIngredientsWithQuantity
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddRecipeContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    modalBottomSheetState: SheetState,
    uiState: AddRecipeState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onIngredientChange: (String) -> Unit,
    onSelectedServings: (Int) -> Unit,
    onServingsPickerDismiss: () -> Unit,
    onServingsPickerSave: () -> Unit,
    onServingsButtonClicked: () -> Unit,
    onSelectedPrepTimeHours: (String) -> Unit,
    onSelectedPrepTimeMinutes: (String) -> Unit,
    onPrepTimePickerDismiss: () -> Unit,
    onPrepTimePickerSave: () -> Unit,
    onPrepTimeButtonClicked: () -> Unit,
    onDropDownMenuExpandedChange: () -> Unit,
    onIngredientSuggestionClick: (Ingredient) -> Unit,
    onAddPhoto: () -> Unit,
    onTakePhoto: () -> Unit,
    onSelectImage: () -> Unit,
    onAddImageDismiss: () -> Unit,
    onReorder: () -> Unit,
    onDragIndexChange: (String) -> Unit,
    onDropIndexChange: (String) -> Unit,
    onDraggedIngredientChange: (String) -> Unit,
    onSwipeToDelete: (Ingredient) -> Unit,
    onIngredientClicked: (String) -> Unit,
    onSelectedWholeQuantity: (String) -> Unit,
    onSelectedDecimalQuantity: (String) -> Unit,
    onSelectedTypeQuantity: (String) -> Unit,
    onQuantityPickerDismiss: () -> Unit,
    onQuantityPickerSave: () -> Unit,
    onCategoriesButtonClicked: () -> Unit,
    onCheckBoxToggled: (Category) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogSave: () -> Unit,
    onGoBack: () -> Unit,
    onAddRecipe: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_recipe)) },
                navigationIcon = {
                    IconButton(onClick = { onGoBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAddRecipe() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add recipe button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .testTag("Add Recipe Content")
        ) {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .testTag("Title")
            )

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { onTitleChange(it) },
                label = { Text(text = stringResource(id = R.string.title)) },
                placeholder = { Text(text = stringResource(id = R.string.title)) },
                supportingText = {
                    if (uiState.titleError != null) {
                        Text(text = uiState.titleError)
                    } },
                isError = uiState.titleError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("Add recipe title TF")
            )

            AddImageCard(
                imageUri = uiState.imageUri,
                onClick = { onAddPhoto() }
            )

            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(bottom = 8.dp)
                    .testTag("Description")
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = { onDescriptionChange(it) },
                label = { Text(text = stringResource(id = R.string.description)) },
                placeholder = { Text(text = stringResource(id = R.string.description)) },
                supportingText = {
                    if (uiState.descriptionError != null) {
                        Text(text = uiState.descriptionError)
                    } },
                isError = uiState.descriptionError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .testTag("Add recipe description TF")
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(
                        text = stringResource(id = R.string.ingredients),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.tap_or_swipe),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Light,

                    )
                }

                TextButton(
                    onClick = { onReorder() },
                    modifier = modifier.testTag("Reorder button"),
                ) {
                    Text(text = stringResource(id = R.string.reorder))
                }
            }

            Column(
                modifier = modifier
                    .padding(bottom = 20.dp)
                    .testTag("Add recipe ingredient list")
            ) {
                uiState.recipeIngredients.forEach { recipeIngredient ->
                    key(recipeIngredient) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    onSwipeToDelete(recipeIngredient.key)
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color =
                                    if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                        Color.Red
                                    } else Color.Transparent

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete icon",
                                        tint = Color.White
                                    )
                                }
                            },
                            content = {
                                RecipeIngredientItem(
                                    ingredient = recipeIngredient.key,
                                    quantity = recipeIngredient.value,
                                    dragIndex = uiState.dragIndex,
                                    isReorderModeActivated = uiState.isReorderModeActivated,
                                    onClick = { onIngredientClicked(it) },
                                    modifier = modifier
                                        .testTag("Recipe Ingredient Item")
                                        .dragAndDropTarget(
                                            shouldStartDragAndDrop = { true },
                                            target = object : DragAndDropTarget {
                                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                                    onDropIndexChange(recipeIngredient.key.ingredientId)
                                                    onDraggedIngredientChange(event.toAndroidDragEvent().clipData.getItemAt(0).text.toString())
                                                    return true
                                                }

                                                override fun onEntered(event: DragAndDropEvent) {
                                                    super.onEntered(event)
                                                    onDragIndexChange(recipeIngredient.key.ingredientId)
                                                }

                                                override fun onExited(event: DragAndDropEvent) {
                                                    super.onExited(event)
                                                    onDragIndexChange("")
                                                }
                                            }
                                        )
                                )
                            }
                        )

                        val isIngredientLastInColumn =
                            uiState.recipeIngredients.keys.indexOf(recipeIngredient.key)!= uiState.recipeIngredients.keys.toList().lastIndex

                        if (isIngredientLastInColumn)
                            HorizontalDivider()
                    }
                }
            }

            AutoComplete(
                expanded = uiState.isDropDownMenuExpanded,
                ingredient = uiState.ingredient,
                ingredients = uiState.ingredientsToSelect,
                onExpandedChange = { onDropDownMenuExpandedChange() },
                onValueChange = { onIngredientChange(it) },
                onClick = { onIngredientSuggestionClick(it) }
            )

            RowWithTextButton(
                sectionName = stringResource(id = R.string.servings),
                buttonText = if(uiState.lastSavedServings == 0) stringResource(id = R.string.set_servings) else uiState.lastSavedServings.toString(),
                onClick = { onServingsButtonClicked() }
            )

            RowWithTextButton(
                sectionName = stringResource(id = R.string.prep_time),
                buttonText = if(uiState.lastSavedPrepTime == "") stringResource(id = R.string.set_time) else uiState.lastSavedPrepTime,
                onClick = { onPrepTimeButtonClicked() }
            )

            RowWithTextButton(
                sectionName = stringResource(id = R.string.categories),
                buttonText = stringResource(id = R.string.set_categories),
                onClick = { onCategoriesButtonClicked() }
            )
        }

        if(uiState.isServingsBottomSheetOpened) {
            ServingsPicker(
                modalSheetState = modalBottomSheetState,
                selectedServings = uiState.selectedServings,
                onSelectedServings = { onSelectedServings(it) },
                onDismiss = { onServingsPickerDismiss() },
                onSave = { onServingsPickerSave() }
            )
        }

        if(uiState.isPrepTimeBottomSheetOpened) {
            PrepTimePicker(
                modalSheetState = modalBottomSheetState,
                selectedPrepTimeHours = uiState.selectedPrepTimeHours,
                selectedPrepTimeMinutes = uiState.selectedPrepTimeMinutes,
                onSelectedPrepTimeHours = { onSelectedPrepTimeHours(it) },
                onSelectedPrepTimeMinutes = { onSelectedPrepTimeMinutes(it) },
                onDismiss = { onPrepTimePickerDismiss() },
                onSave = { onPrepTimePickerSave() }
            )
        }

        if(uiState.isImageBottomSheetOpened) {
            ImagePicker(
                modalSheetState = modalBottomSheetState,
                onDismiss = { onAddImageDismiss() },
                onTakePhoto = { onTakePhoto() },
                onSelectImage = { onSelectImage() }
            )
        }

        if(uiState.isQuantityBottomSheetOpened) {
            QuantityPicker(
                modalSheetState = modalBottomSheetState,
                selectedWholeQuantity = uiState.selectedWholeQuantity,
                selectedDecimalQuantity = uiState.selectedDecimalQuantity,
                selectedTypeQuantity = uiState.selectedTypeQuantity,
                onSelectedWholeQuantity = { onSelectedWholeQuantity(it) },
                onSelectedDecimalQuantity = { onSelectedDecimalQuantity(it) },
                onSelectedTypeQuantity = { onSelectedTypeQuantity(it) },
                onDismiss = { onQuantityPickerDismiss() },
                onSave = { onQuantityPickerSave() }
            )
        }

        if(uiState.isCategoriesDialogActivated) {
            CategoriesDialog(
                categories = uiState.categories,
                onCheckBoxToggled = { onCheckBoxToggled(it) },
                onDismiss = { onDialogDismiss() },
                onSave = { onDialogSave() }
            )
        }
    }
}

private fun getUiState(): AddRecipeState {
    return AddRecipeState(
        isServingsBottomSheetOpened = false,
        selectedServings = 1,
        lastSavedServings = 0,
        isPrepTimeBottomSheetOpened = false,
        selectedPrepTimeHours = "",
        selectedPrepTimeMinutes = "",
        lastSavedPrepTime = "",
        title = "New recipe title",
        titleError = null,
        description = "Description of the new recipe.",
        descriptionError = null,
        ingredient = "ingredient",
        ingredientsToSelect = emptyList(),
        recipeIngredients = getIngredientsWithQuantity(),
        isDropDownMenuExpanded = false,
        isImageBottomSheetOpened = false,
        imageUri = Uri.EMPTY,
        dragIndex = "",
        isReorderModeActivated = false,
        isQuantityBottomSheetOpened = false,
        selectedWholeQuantity = "",
        selectedDecimalQuantity = "",
        selectedTypeQuantity = "",
        isCategoriesDialogActivated = false
    )
}

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddRecipeContentPreview() {
    RecipeAppTheme {
        AddRecipeContent(
            scrollState = rememberScrollState(),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = getUiState(),
            onIngredientChange = {},
            onTitleChange = {},
            onDescriptionChange = {},
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
            onDragIndexChange = {},
            onDropIndexChange = {},
            onDraggedIngredientChange = {},
            onSwipeToDelete = {},
            onReorder = {},
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

@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddRecipeContentPreviewErrorsShown() {
    RecipeAppTheme {
        AddRecipeContent(
            scrollState = rememberScrollState(),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = getUiState(),
            onIngredientChange = {},
            onTitleChange = {},
            onDescriptionChange = {},
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
            onDragIndexChange = {},
            onDropIndexChange = {},
            onDraggedIngredientChange = {},
            onSwipeToDelete = {},
            onReorder = {},
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
            onAddRecipe = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddRecipeContentPreviewBottomSheetOpen() {
    RecipeAppTheme {
        AddRecipeContent(
            scrollState = rememberScrollState(),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = AddRecipeState(isServingsBottomSheetOpened = true),
            onIngredientChange = {},
            onTitleChange = {},
            onDescriptionChange = {},
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
            onDragIndexChange = {},
            onDropIndexChange = {},
            onDraggedIngredientChange = {},
            onSwipeToDelete = {},
            onReorder = {},
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddRecipeContentPreviewCategoriesDialog() {
    RecipeAppTheme {
        AddRecipeContent(
            scrollState = rememberScrollState(),
            modalBottomSheetState = rememberModalBottomSheetState(),
            uiState = AddRecipeState(isCategoriesDialogActivated = true),
            onIngredientChange = {},
            onTitleChange = {},
            onDescriptionChange = {},
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
            onDragIndexChange = {},
            onDropIndexChange = {},
            onDraggedIngredientChange = {},
            onSwipeToDelete = {},
            onReorder = {},
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