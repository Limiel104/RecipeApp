package com.example.recipeapp.presentation.add_recipe

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val validateFieldUseCase: ValidateFieldUseCase,
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val addImageUseCase: AddImageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addRecipeUseCase: AddRecipeUseCase
): ViewModel() {

    private val _addRecipeState = mutableStateOf(AddRecipeState())
    val addRecipeState: State<AddRecipeState> = _addRecipeState

    private val _addRecipeUiEventChannel = Channel<AddRecipeUiEvent>()
    val addRecipeUiEventChannelFlow = _addRecipeUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "Add recipe VM")
        getIngredients()
        getCategories()
    }

    fun onEvent(event: AddRecipeEvent) {
        when(event) {
            is AddRecipeEvent.EnteredTitle -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    title = event.title
                )
            }

            is AddRecipeEvent.EnteredDescription -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    description = event.description
                )
            }

            is AddRecipeEvent.EnteredIngredient -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    ingredient = event.ingredient
                )
            }

            is AddRecipeEvent.SelectedServings -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedServings = event.servings
                )
            }

            is AddRecipeEvent.SelectedPrepTimeHours -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedPrepTimeHours = event.hours
                )
            }

            is AddRecipeEvent.SelectedPrepTimeMinutes -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedPrepTimeMinutes = event.minutes
                )
            }

            is AddRecipeEvent.SelectedIngredient -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isDropDownMenuExpanded = false
                )

                addIngredientToRecipeIngredientList(event.ingredient)
            }

            is AddRecipeEvent.SelectedRecipeImage -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    imageUri = event.imageUri
                )
            }

            is AddRecipeEvent.PreparedTempUri -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    tempUri = event.tempUri
                )

                viewModelScope.launch { _addRecipeUiEventChannel.send(AddRecipeUiEvent.LaunchCamera(_addRecipeState.value.tempUri)) }
            }

            is AddRecipeEvent.OnDragIndexChange -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    dragIndex = event.dragIndex,
                    dropIndex = ""
                )
            }

            is AddRecipeEvent.OnDropIndexChange -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    dragIndex = "",
                    dropIndex = event.dropIndex
                )
            }

            is AddRecipeEvent.OnDraggedIngredientChange -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    draggedIngredientId = event.draggedIngredientId
                )

                reorderRecipeIngredientList(
                    _addRecipeState.value.dropIndex,
                    _addRecipeState.value.draggedIngredientId
                )
            }

            is AddRecipeEvent.OnSwipeToDelete -> {
                deleteIngredientFromRecipeIngredientList(event.ingredient)
            }

            is AddRecipeEvent.OnIngredientClicked -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedIngredientId = event.ingredientId,
                    isQuantityBottomSheetOpened = true
                )
            }

            is AddRecipeEvent.SelectedWholeQuantity -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedWholeQuantity = event.whole
                )
            }

            is AddRecipeEvent.SelectedDecimalQuantity -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedDecimalQuantity = event.decimal
                )
            }

            is AddRecipeEvent.SelectedTypeQuantity -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedTypeQuantity = event.type
                )
            }

            is AddRecipeEvent.OnCheckBoxToggled -> {
                toggleCheckBox(event.category)
            }

            AddRecipeEvent.OnServingsPickerDismissed -> {
                if(_addRecipeState.value.lastSavedServings != 0) {
                    _addRecipeState.value = addRecipeState.value.copy(
                        isServingsBottomSheetOpened = false,
                        selectedServings = _addRecipeState.value.lastSavedServings
                    )
                }
                else {
                    _addRecipeState.value = addRecipeState.value.copy(
                        selectedServings = 0,
                        isServingsBottomSheetOpened = false
                    )
                }
            }

            AddRecipeEvent.OnServingsPickerSaved -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isServingsBottomSheetOpened = false,
                    lastSavedServings = _addRecipeState.value.selectedServings
                )
            }

            AddRecipeEvent.OnServingsButtonClicked -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isServingsBottomSheetOpened = true
                )
            }

            AddRecipeEvent.OnPrepTimePickerDismissed -> {
                if(_addRecipeState.value.lastSavedPrepTime != "") {
                    _addRecipeState.value = addRecipeState.value.copy(
                        isPrepTimeBottomSheetOpened = false,
                        selectedPrepTimeHours = _addRecipeState.value.lastSavedPrepTimeHours,
                        selectedPrepTimeMinutes = _addRecipeState.value.lastSavedPrepTimeMinutes
                    )
                }
                else {
                    _addRecipeState.value = addRecipeState.value.copy(
                        selectedPrepTimeHours = "",
                        selectedPrepTimeMinutes = "",
                        isPrepTimeBottomSheetOpened = false
                    )
                }
            }

            AddRecipeEvent.OnPrepTimePickerSaved -> {
                val hours = _addRecipeState.value.selectedPrepTimeHours
                val minutes = _addRecipeState.value.selectedPrepTimeMinutes
                _addRecipeState.value = addRecipeState.value.copy(
                    isPrepTimeBottomSheetOpened = false,
                    lastSavedPrepTimeHours = hours,
                    lastSavedPrepTimeMinutes = minutes,
                    lastSavedPrepTime = getPrepTime(hours, minutes)
                )
            }

            AddRecipeEvent.OnPrepTimeButtonClicked -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isPrepTimeBottomSheetOpened = true
                )
            }

            AddRecipeEvent.OnDropDownMenuExpandChange -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isDropDownMenuExpanded = !_addRecipeState.value.isDropDownMenuExpanded
                )
            }

            AddRecipeEvent.OnAddImage -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpened = true
                )
            }

            AddRecipeEvent.OnTakePhoto -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpened = false
                )

                viewModelScope.launch { _addRecipeUiEventChannel.send(AddRecipeUiEvent.LaunchGetPermission) }
            }

            AddRecipeEvent.OnSelectImage -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpened = false
                )

                viewModelScope.launch { _addRecipeUiEventChannel.send(AddRecipeUiEvent.LaunchGallery) }
            }

            AddRecipeEvent.OnAddImageDismiss -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpened = false
                )
            }

            AddRecipeEvent.OnReorder -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isReorderModeActivated = !_addRecipeState.value.isReorderModeActivated
                )
            }

            AddRecipeEvent.OnQuantityPickerDismissed -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    selectedWholeQuantity = "",
                    selectedDecimalQuantity = "",
                    selectedTypeQuantity = "",
                    isQuantityBottomSheetOpened = false
                )
            }

            AddRecipeEvent.OnQuantityPickerSaved -> {
                val ingredient = _addRecipeState.value.recipeIngredients.keys.find { ingredient ->
                    ingredient.ingredientId == _addRecipeState.value.selectedIngredientId
                }

                val quantity = getIngredientQuantity(
                    _addRecipeState.value.selectedWholeQuantity,
                    _addRecipeState.value.selectedDecimalQuantity,
                    _addRecipeState.value.selectedTypeQuantity
                )

                val recipeIngredients = getTempMap(_addRecipeState.value.recipeIngredients)

                ingredient?.let {
                    if(recipeIngredients.keys.contains(ingredient))
                        recipeIngredients.replace(it, quantity)
                    else
                        recipeIngredients.put(it, quantity)
                }

                _addRecipeState.value = addRecipeState.value.copy(
                    recipeIngredients = recipeIngredients,
                    selectedWholeQuantity = "",
                    selectedDecimalQuantity = "",
                    selectedTypeQuantity = "",
                    isQuantityBottomSheetOpened = false
                )
            }

            AddRecipeEvent.OnCategoriesButtonClicked -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isCategoriesDialogActivated = true
                )
            }

            AddRecipeEvent.OnDialogDismiss -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isCategoriesDialogActivated = false,
                    categories = _addRecipeState.value.lastSavedCategories,
                )
            }

            AddRecipeEvent.OnDialogSave -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isCategoriesDialogActivated = false,
                    lastSavedCategories = _addRecipeState.value.categories
                )
            }

            AddRecipeEvent.OnAddRecipe -> {
                val title = _addRecipeState.value.title
                val description = _addRecipeState.value.description
                val imageName = System.currentTimeMillis().toString()+"_"+UUID.randomUUID().toString()+".jpg"

                if(isValidationSuccessful(title, description)) {
                    if(_addRecipeState.value.imageUri != Uri.EMPTY)
                        _addRecipeState.value.imageUri?.let { addImage(it, imageName) }
                    else
                        addRecipe()
                }
                else
                    Log.i("TAG", "Form validation error")
            }
        }
    }

    private fun isValidationSuccessful(title: String, description: String): Boolean {
        val titleValidationResult = validateFieldUseCase(title)
        val descriptionValidationResult = validateFieldUseCase(description)

        val hasError = listOf(
            titleValidationResult,
            descriptionValidationResult
        ).any { !it.isSuccessful }

        if (hasError) {
            _addRecipeState.value = addRecipeState.value.copy(
                titleError = titleValidationResult.errorMessage,
                descriptionError = descriptionValidationResult.errorMessage
            )
            return false
        }

        _addRecipeState.value = addRecipeState.value.copy(
            titleError = null,
            descriptionError = null
        )
        return true
    }

    private fun getTempMap(recipeIngredients: Map<Ingredient, Quantity>): MutableMap<Ingredient, Quantity> {
        val tempMap = mutableMapOf<Ingredient, Quantity>()
        for(recipeIngredient in recipeIngredients) {
            tempMap[recipeIngredient.key] = recipeIngredient.value
        }
        return tempMap
    }

    private fun addIngredientToMapAtIndex(
        recipeIngredients: Map<Ingredient, Quantity>,
        index: Int,
        newIngredient: Ingredient,
        quantity: Quantity
    ): Map<Ingredient, Quantity> {
        val tempMap = mutableMapOf<Ingredient, Quantity>()

        if(index == recipeIngredients.size) {
            for(ingredient in recipeIngredients)
                tempMap[ingredient.key] = ingredient.value
            tempMap[newIngredient] = quantity
        }
        else {
            for(ingredient in recipeIngredients) {
                if(tempMap.size != index)
                    tempMap[ingredient.key] = ingredient.value
                else {
                    tempMap[newIngredient] = quantity
                    tempMap[ingredient.key] = ingredient.value
                }
            }
        }
        return tempMap
    }

    private fun getCurrentIngredients(recipeIngredients: Map<Ingredient, Quantity>): List<Ingredient> {
        val allIngredients = _addRecipeState.value.allIngredients

        return allIngredients.filter { ingredient -> recipeIngredients.all { recipeIngredient ->  recipeIngredient.key.ingredientId != ingredient.ingredientId}  }
    }

    private fun addIngredientToRecipeIngredientList(newIngredient: Ingredient) {
        val recipeIngredients = getTempMap(_addRecipeState.value.recipeIngredients)

        recipeIngredients[newIngredient] = ""

        _addRecipeState.value = addRecipeState.value.copy(
            ingredientsToSelect = getCurrentIngredients(recipeIngredients),
            recipeIngredients = recipeIngredients
        )
    }

    private fun reorderRecipeIngredientList(dropIndex: String, draggedIngredientId: String) {
        val recipeIngredients = getTempMap(_addRecipeState.value.recipeIngredients)
        val dragIngredient = recipeIngredients.keys.find { ingredient -> ingredient.ingredientId == draggedIngredientId }
        val dropIngredient = recipeIngredients.keys.find { ingredient -> ingredient.ingredientId == dropIndex }
        recipeIngredients.keys.onEachIndexed { index, ingredient ->
            if(ingredient  == dropIngredient) {
                _addRecipeState.value = addRecipeState.value.copy(
                    index = index
                )
            }
        }
        val dropQuantity = recipeIngredients[dragIngredient]

        recipeIngredients.remove(dragIngredient)
        val reorderedRecipeIngredients = dragIngredient?.let { ingredient ->
            dropQuantity?.let { quantity ->
                addIngredientToMapAtIndex(
                    recipeIngredients, _addRecipeState.value.index, ingredient, quantity
                )
            }
        }

        reorderedRecipeIngredients?.let {
            _addRecipeState.value = addRecipeState.value.copy(
                recipeIngredients = emptyMap() //equal returns true so no update
            )

            _addRecipeState.value = addRecipeState.value.copy(
                recipeIngredients = it
            )
        }
    }

    private fun deleteIngredientFromRecipeIngredientList(ingredient: Ingredient) {
        val recipeIngredients = getTempMap(_addRecipeState.value.recipeIngredients)

        recipeIngredients.remove(ingredient)

        _addRecipeState.value = addRecipeState.value.copy(
            recipeIngredients = recipeIngredients,
            ingredientsToSelect = getCurrentIngredients(recipeIngredients)
        )
    }

    private fun toggleCheckBox(categoryToToggle: Category) {
        val categories = mutableMapOf<Category, Boolean>()
        for(category in _addRecipeState.value.categories) {
            categories[category.key] = category.value
        }

        val oldValue = categories[categoryToToggle]
        oldValue?.let { categories[categoryToToggle] = !it }

        _addRecipeState.value = addRecipeState.value.copy(
            categories = categories
        )
    }

    private fun getRecipeCategories(): List<String> {
        val recipeCategories = _addRecipeState.value.lastSavedCategories.filter { category ->
            category.value
        }.keys.toList()

        val categoryIds = mutableListOf<String>()
        for(category in recipeCategories) {
            categoryIds.add(category.categoryId)
        }

        return categoryIds
    }

    private fun getPrepTime(hours: String, minutes: String): String {
        return if (hours != "" && minutes != "") "$hours $minutes"
        else if (hours != "") hours
        else if (minutes != "") minutes
        else ""
    }

    private fun getIngredientQuantity(whole: String, decimal: String, type: String): String {
        return if (whole != "" && decimal != "" && type != "") "$whole$decimal $type"
        else if(whole != "" && decimal != "") "$whole$decimal"
        else if(whole != "" && type != "") "$whole $type"
        else if(decimal != "" && type != "") "$decimal $type"
        else if(whole != "" ) whole
        else if(decimal != "") decimal
        else if(type != "") type
        else ""
    }

    private fun getIngredients() {
        viewModelScope.launch {
            getIngredientsUseCase().collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getIngredients: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading get ingredients: ${response.isLoading}")
                        _addRecipeState.value = addRecipeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            _addRecipeState.value = addRecipeState.value.copy(
                                ingredientsToSelect = response.data,
                                allIngredients = response.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addImage(imageUri: Uri, imageName: String) {
        viewModelScope.launch {
            addImageUseCase(imageUri, imageName).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from addImage: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading add image: ${response.isLoading}")
                        _addRecipeState.value = addRecipeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG",response.data.toString())
                        response.data?.let {
                            Log.i("TAG", "image url vm: ${response.data}")
                            _addRecipeState.value = addRecipeState.value.copy(
                                imageUrl = response.data.toString()
                            )
                            addRecipe()
                        }
                    }
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG","Error message from getCategories: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG","Loading categories: ${response.isLoading}")
                        _addRecipeState.value = addRecipeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        response.data?.let {
                            _addRecipeState.value = addRecipeState.value.copy(
                                categories = response.data.associateWith { false },
                                lastSavedCategories = response.data.associateWith { false }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addRecipe() {
        val recipeWithIngredients = RecipeWithIngredients(
            recipeId = "",
            name = _addRecipeState.value.title,
            ingredients = _addRecipeState.value.recipeIngredients,
            prepTime = _addRecipeState.value.lastSavedPrepTime,
            servings = _addRecipeState.value.lastSavedServings,
            description = _addRecipeState.value.description,
            isVegetarian = false,
            isVegan = false,
            imageUrl = _addRecipeState.value.imageUrl,
            createdBy = getCurrentUserUseCase()!!.uid,
            categories = getRecipeCategories(),
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            addRecipeUseCase(recipeWithIngredients).collect { response ->
                when(response) {
                    is Resource.Error -> {
                        Log.i("TAG", "Error message from addRecipe: ${response.message}")
                    }
                    is Resource.Loading -> {
                        Log.i("TAG", "Loading add recipe: ${response.isLoading}")
                        _addRecipeState.value = addRecipeState.value.copy(
                            isLoading = response.isLoading
                        )
                    }
                    is Resource.Success -> {
                        Log.i("TAG", response.data.toString())
                    }
                }
            }
        }
    }
}