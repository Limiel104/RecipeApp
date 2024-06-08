package com.example.recipeapp.presentation.add_recipe

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.domain.model.Ingredient
import com.example.recipeapp.domain.model.Resource
import com.example.recipeapp.domain.use_case.AddImageUseCase
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
): ViewModel() {

    private val _addRecipeState = mutableStateOf(AddRecipeState())
    val addRecipeState: State<AddRecipeState> = _addRecipeState

    private val _addRecipeUiEventChannel = Channel<AddRecipeUiEvent>()
    val addRecipeUiEventChannelFlow = _addRecipeUiEventChannel.receiveAsFlow()

    init {
        Log.i("TAG", "Add recipe VM")
        getIngredients()
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
                    isDropDownMenuExpanded = false,
                    recipeIngredients = getRecipeIngredients(event.ingredient, _addRecipeState.value.recipeIngredients)
                )
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
                        selectedPrepTimeMinutes = _addRecipeState.value.lastSavedPrepMinutes
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
                    lastSavedPrepMinutes = minutes,
                    lastSavedPrepTime = "$hours $minutes"
                )
            }

            AddRecipeEvent.OnPrepTimeButtonClicked -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isPrepTimeBottomSheetOpened = true
                )
            }

            AddRecipeEvent.OnExpandChange -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isDropDownMenuExpanded = !_addRecipeState.value.isDropDownMenuExpanded
                )
            }

            AddRecipeEvent.OnAddImage -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpen = true
                )
            }

            AddRecipeEvent.OnTakePhoto -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpen = false
                )

                viewModelScope.launch { _addRecipeUiEventChannel.send(AddRecipeUiEvent.LaunchGetPermission) }
            }

            AddRecipeEvent.OnSelectImage -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpen = false
                )

                viewModelScope.launch { _addRecipeUiEventChannel.send(AddRecipeUiEvent.LaunchGallery) }
            }

            AddRecipeEvent.OnAddImageDismiss -> {
                _addRecipeState.value = addRecipeState.value.copy(
                    isImageBottomSheetOpen = false
                )
            }

            AddRecipeEvent.OnAddRecipe -> {
                val title = _addRecipeState.value.title
                val description = _addRecipeState.value.description
                val imageName = System.currentTimeMillis().toString()+"_"+UUID.randomUUID().toString()+".jpg"

                _addRecipeState.value.imageUri?.let { addImage(it, imageName) }

                if(isValidationSuccessful(title, description))
                    addRecipe()
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

    private fun getRecipeIngredients(newIngredient: Ingredient, ingredients: List<Ingredient>): List<Ingredient> {
        val recipeIngredients = mutableListOf<Ingredient>()

        for(ingredient in ingredients) {
            recipeIngredients.add(ingredient)
        }
        recipeIngredients.add(newIngredient)
        return recipeIngredients
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
                                ingredients = response.data
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
                        }
                    }
                }
            }
        }
    }

    private fun addRecipe() {
        Log.i("TAG", "Add recipe")
    }
}