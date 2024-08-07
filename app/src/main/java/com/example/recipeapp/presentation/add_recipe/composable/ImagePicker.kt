@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.R
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
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onTakePhoto() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = modifier.padding(end = 12.dp),
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take photo icon"
                )

                Text(
                    text = stringResource(id = R.string.take_photo),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp)
                    .clickable { onSelectImage() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = modifier.padding(end = 12.dp),
                    imageVector = Icons.Default.Image,
                    contentDescription = "Select image icon"
                )

                Text(
                    text = stringResource(id = R.string.select_image),
                    style = MaterialTheme.typography.titleMedium
                )
            }
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