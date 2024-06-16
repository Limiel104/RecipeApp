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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
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
    val uiState = viewModel.addRecipeState.value

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val authority = stringResource(id = R.string.fileprovider)
    val directory = File(context.cacheDir, "images")

    fun getTempUri(): Uri? {
        directory.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )
            return FileProvider.getUriForFile(context, authority, file)
        }
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        viewModel.onEvent(AddRecipeEvent.SelectedRecipeImage(result.uriContent))
    }

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { result ->
        result?.let {
            val cropOptions = CropImageContractOptions(result, uiState.cropImageOptions)
            imageCropLauncher.launch(cropOptions)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { isSaved ->
        if(isSaved) {
            val cropOptions = CropImageContractOptions(uiState.tempUri, uiState.cropImageOptions)
            imageCropLauncher.launch(cropOptions)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if(isGranted)
            viewModel.onEvent(AddRecipeEvent.PreparedTempUri(getTempUri()))
        // else: Permission is denied, handle it accordingly
    }

    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.addRecipeUiEventChannelFlow.collectLatest { event ->
                Log.i("TAG", "Add recipe screen LE")
                when(event) {
                    is AddRecipeUiEvent.LaunchCamera -> {
                        cameraLauncher.launch(event.tempUri)
                    }

                    AddRecipeUiEvent.LaunchGetPermission -> {
                        val permission = Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.onEvent(AddRecipeEvent.PreparedTempUri(getTempUri()))

                        } else {
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
        uiState = uiState,
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
        onAddPhoto = { viewModel.onEvent(AddRecipeEvent.OnAddImage) },
        onTakePhoto = {  viewModel.onEvent(AddRecipeEvent.OnTakePhoto) },
        onSelectImage = {  viewModel.onEvent(AddRecipeEvent.OnSelectImage) },
        onReorder = { viewModel.onEvent(AddRecipeEvent.OnReorder) },
        onAddImageDismiss = { viewModel.onEvent(AddRecipeEvent.OnAddImageDismiss) },
        onDragIndexChange = {viewModel.onEvent(AddRecipeEvent.OnDragIndexChange(it))},
        onDropIndexChange = { viewModel.onEvent(AddRecipeEvent.OnDropIndexChange(it)) },
        onDraggedIngredientChange = { viewModel.onEvent(AddRecipeEvent.OnDraggedIngredientChange(it)) },
        onSwipeToDelete = { viewModel.onEvent(AddRecipeEvent.OnSwipeToDelete(it)) },
        onIngredientClicked = { viewModel.onEvent(AddRecipeEvent.OnIngredientClicked(it)) },
        onSelectedWholeQuantity = { viewModel.onEvent(AddRecipeEvent.SelectedWholeQuantity(it)) },
        onSelectedDecimalQuantity = { viewModel.onEvent(AddRecipeEvent.SelectedDecimalQuantity(it)) },
        onSelectedTypeQuantity = { viewModel.onEvent(AddRecipeEvent.SelectedTypeQuantity(it)) },
        onQuantityPickerDismiss = { viewModel.onEvent(AddRecipeEvent.OnQuantityPickerDismissed) },
        onQuantityPickerSave = { viewModel.onEvent(AddRecipeEvent.OnQuantityPickerSaved) },
        onCategoriesButtonClicked = {},
        onAddRecipe = { viewModel.onEvent(AddRecipeEvent.OnAddRecipe) },
    )
}