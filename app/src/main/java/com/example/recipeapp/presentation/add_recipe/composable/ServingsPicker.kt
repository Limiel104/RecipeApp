package com.example.recipeapp.presentation.add_recipe.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.recipeapp.R
import com.example.recipeapp.ui.theme.RecipeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServingsPicker(
    modifier: Modifier = Modifier,
    modalSheetState: SheetState,
    selectedServings: Int,
    onSelectedServings: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalSheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("Servings picker"),
        ) {
            Text(
                text = stringResource(id = R.string.servings),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(id = R.string.servings_sheet_subtext),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Light,
                modifier = modifier.padding(bottom = 20.dp)
            )

            NumberPicker(
                value = selectedServings,
                range = 1..25,
                onValueChange = { onSelectedServings(it) },
                dividersColor = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .fillMaxWidth()
                    .testTag("Servings number picker")
            )

            Button(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
                    .testTag("Save servings button"),
                onClick = { onSave() }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
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
fun ServingsPickerPreview() {
    RecipeAppTheme {
        val modalSheetState = rememberModalBottomSheetState()

        ServingsPicker(
            modalSheetState = modalSheetState,
            selectedServings = 4,
            onSelectedServings = {},
            onDismiss = {},
            onSave = {}
        )
    }
}