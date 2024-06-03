@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipeapp.presentation.add_recipe.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotoPicker(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    onDismiss: () -> Unit,
    onTakePhoto: () -> Unit,
    onSelectImage: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextButton(onClick = { onTakePhoto() }) {
                Text(text = "Take Photo")
            }

            TextButton(onClick = { onSelectImage() }) {
                Text(text = "Select Image")
            }
        }
    }
}