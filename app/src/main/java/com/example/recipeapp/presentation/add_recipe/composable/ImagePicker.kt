@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
import com.example.recipeapp.presentation.common.composable.MenuItem
import com.example.recipeapp.ui.theme.RecipeAppTheme

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    onDismiss: () -> Unit,
    onTakePhoto: () -> Unit,
    onSelectImage: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
        modifier = modifier.testTag("Image picker")
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            MenuItem(
                icon = Icons.Default.Camera,
                text = stringResource(id = R.string.take_photo),
                onClick = { onTakePhoto() }
            )

            MenuItem(
                icon = Icons.Default.Image,
                text = stringResource(id = R.string.select_image),
                onClick = { onSelectImage() },
                modifier = modifier.padding(bottom = 36.dp)
            )
        }
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
fun ImagePickerPreview() {
    RecipeAppTheme {
        Surface {
            ImagePicker(
                modalSheetState = rememberModalBottomSheetState(),
                onDismiss = {},
                onTakePhoto = {}) {
            }
        }
    }
}