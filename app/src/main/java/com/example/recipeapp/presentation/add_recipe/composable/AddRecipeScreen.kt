package com.example.recipeapp.presentation.add_recipe.composable

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.recipeapp.R
import com.example.recipeapp.presentation.add_recipe.AddRecipeEvent
import com.example.recipeapp.presentation.add_recipe.AddRecipeUiEvent
import com.example.recipeapp.presentation.add_recipe.AddRecipeViewModel
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val modalBottomSheetState = rememberModalBottomSheetState()
    val isServingsBottomSheetOpen = viewModel.addRecipeState.value.isServingsBottomSheetOpened
    val selectedServings = viewModel.addRecipeState.value.selectedServings
    val lastSavedServings = viewModel.addRecipeState.value.lastSavedServings
    val isPrepTimeBottomSheetOpen = viewModel.addRecipeState.value.isPrepTimeBottomSheetOpened
    val selectedPrepTimeMinutes = viewModel.addRecipeState.value.selectedPrepTimeMinutes
    val selectedPrepTimeHours = viewModel.addRecipeState.value.selectedPrepTimeHours
    val lastSavedPrepTime = viewModel.addRecipeState.value.lastSavedPrepTime
    val title = viewModel.addRecipeState.value.title
    val titleError = viewModel.addRecipeState.value.titleError
    val description = viewModel.addRecipeState.value.description
    val descriptionError = viewModel.addRecipeState.value.descriptionError
    val ingredient = viewModel.addRecipeState.value.ingredient
    val ingredients = viewModel.addRecipeState.value.ingredients
    val isDropDownMenuExpanded = viewModel.addRecipeState.value.isDropDownMenuExpanded
    val recipeIngredients = viewModel.addRecipeState.value.recipeIngredients
//    val tempUri = viewModel.addRecipeState.value.tempUri
    val isPhotoBottomSheetOpen = viewModel.addRecipeState.value.isPhotoBottomSheetOpen
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val authority = stringResource(id = R.string.fileprovider)

    val tempUri = remember { mutableStateOf<Uri?>(null) }

    fun getTempUri(directory: File? = null): Uri? {
        directory?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )
            return FileProvider.getUriForFile(context, authority, file)
        }
        return null
    }

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { result ->
        viewModel.onEvent(AddRecipeEvent.SelectedRecipePicture(result))
    }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { isSaved ->
//        viewModel.onEvent(AddRecipeEvent.SelectedRecipePicture(tempUri.value))
        Log.i("TAG", "elo elo 3 2 0")
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
//            viewModel.onEvent(AddRecipeEvent.PreparedTempUri(getTempUri()))
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            cameraLauncher.launch(tempUri.value)
        }
        else {
            // Permission is denied, handle it accordingly
        }
    }

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.addRecipeUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Add recipe screen LE")
                when(event) {
                    AddRecipeUiEvent.LaunchCamera -> {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Permission is already granted, proceed to step 2
                            val tmpUri = getTempUri()
                            tempUri.value = tmpUri
                            cameraLauncher.launch(tempUri.value)
                        } else {
                            // Permission is not granted, request it
                            cameraPermissionLauncher.launch(permission)
                        }
                    }
                    AddRecipeUiEvent.LaunchGallery -> {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
            }
        }
    }

    AddRecipeContent(
        scrollState = scrollState,
        modalBottomSheetState = modalBottomSheetState,
        isServingsBottomSheetOpen = isServingsBottomSheetOpen,
        selectedServings = selectedServings,
        lastSavedServings = lastSavedServings,
        isPrepTimeBottomSheetOpen = isPrepTimeBottomSheetOpen,
        selectedPrepTimeHours = selectedPrepTimeHours,
        selectedPrepTimeMinutes = selectedPrepTimeMinutes,
        lastSavedPrepTime = lastSavedPrepTime,
        title = title,
        titleError = titleError,
        description = description,
        descriptionError = descriptionError,
        ingredient = ingredient,
        ingredients = ingredients,
        recipeIngredients = recipeIngredients,
        isDropDownMenuExpanded = isDropDownMenuExpanded,
        isPhotoBottomSheetOpen = isPhotoBottomSheetOpen,
        onTitleChange = { viewModel.onEvent(AddRecipeEvent.EnteredTitle(it)) },
        onDescriptionChange = { viewModel.onEvent(AddRecipeEvent.EnteredDescription(it)) },
        onIngredientChange = { viewModel.onEvent(AddRecipeEvent.EnteredIngredient(it)) },
        onSelectedServings = { viewModel.onEvent(AddRecipeEvent.SelectedServings(it)) },
        onServingsPickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerDismissed) },
        onServingsPickerSave = { viewModel.onEvent(AddRecipeEvent.OnServingsPickerSaved) },
        onServingsButtonClicked = { viewModel.onEvent(AddRecipeEvent.OnServingsButtonClicked) },
        onSelectedPrepTimeHours = { viewModel.onEvent(AddRecipeEvent.SelectedPrepTimeHours(it)) },
        onSelectedPrepTimeMinutes = { viewModel.onEvent(AddRecipeEvent.SelectedPrepTimeMinutes(it)) },
        onPrepTimePickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnPrepTimePickerDismissed) },
        onPrepTimePickerSave = { viewModel.onEvent(AddRecipeEvent.OnPrepTimePickerSaved) },
        onPrepTimeButtonClicked = { viewModel.onEvent(AddRecipeEvent.OnPrepTimeButtonClicked) },
        onExpandedChange = { viewModel.onEvent(AddRecipeEvent.OnExpandChange) },
        onIngredientSuggestionClick = {viewModel.onEvent(AddRecipeEvent.SelectedIngredient(it))},
        onAddPhoto = { viewModel.onEvent(AddRecipeEvent.OnAddPhoto) },
        onTakePhoto = {  viewModel.onEvent(AddRecipeEvent.OnTakePhoto) },
        onSelectImage = {  viewModel.onEvent(AddRecipeEvent.OnSelectImage) },
        onAddRecipe = { viewModel.onEvent(AddRecipeEvent.OnAddRecipe) },
    )
}